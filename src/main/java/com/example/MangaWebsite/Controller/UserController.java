package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.CustomUserDetail;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "User/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        request.getSession().invalidate();
        return "redirect:/";
    }


    @PostMapping("/login")
    public String login(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "User/login";
        }
    }


    @GetMapping("/Register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "User/signup";
    }

    @PostMapping("/Register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> model.addAttribute(error.getField() + "_error", error.getDefaultMessage()));
            return "User/signup";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }

    //Profile


    @GetMapping("/profile")
    public String showUserProfile(Model model, Authentication authentication) {
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        Long userId = userDetails.getId();
        User user = userService.getUserbyId(userId);
        String[] role = userService.getUserRolebyId(userId);

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("role", role);
            return "User/Profile";
        } else {
            return "redirect:/Profile?error";
        }
    }

    private String sanitizeFileName(String fileName) {
        String fileNameWithoutAccent = convertToAscii(fileName);
        return fileNameWithoutAccent.replaceAll("\\s", "_");
    }

    private String convertToAscii(String input) {
        return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    //edit
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Optional<User> editUser = Optional.ofNullable(userService.getUserbyId(id));

        if (editUser.isPresent()) {
            User user = editUser.get();
            model.addAttribute("user", user);
            return "/EditUser";
        } else
            return "redirect:/error";
    }

    @PostMapping("/edit")
    public String editform(@ModelAttribute("user") User updatedUser,
                           @RequestParam("avatar_url") MultipartFile avatarFile,
                           BindingResult result,
                           Model model,
                           Authentication authentication) {
        User existingUser = userService.getUserbyId(updatedUser.getId());
        if (result.hasErrors()) {
            return "redirect:/profile?error";
        }
        try {
            if (avatarFile.isEmpty()) {
                return "redirect:/profile?error";
            }
            long maxSize = 10 * 1024 * 1024; // 10MB
            if (avatarFile.getSize() > maxSize) {
                return "redirect:/profile?error";
            }
            String allowedContentType = "image/*";
            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                return "redirect:/profile?error";
            }
            String fileName = avatarFile.getOriginalFilename();
            String sanitizedFileName = sanitizeFileName(fileName);
            Path directoryPath = Paths.get("E:/GCWT2_User", sanitizeFileName(existingUser.getId().toString()));
            Path filePath = directoryPath.resolve(sanitizedFileName);
            String Avatar_url = sanitizeFileName(existingUser.getId().toString() +'/' + sanitizedFileName);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            if (existingUser != null) {
                if (updatedUser.getCCCD() != null) {
                    existingUser.setCCCD(updatedUser.getCCCD());
                }
                if (updatedUser.getSDT() != null) {
                    existingUser.setSDT(updatedUser.getSDT());
                }
                if (updatedUser.getNgaysinh() != null) {
                    existingUser.setNgaysinh(updatedUser.getNgaysinh());
                }
                if (updatedUser.getDisplayname() != null) {
                    existingUser.setDisplayname(updatedUser.getDisplayname());
                }
                Files.write(filePath, avatarFile.getBytes());
                existingUser.setAvatarUserFileName(Avatar_url);
                userService.updateUser(existingUser);
                return "User/Profile";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/profile";
    }
}


