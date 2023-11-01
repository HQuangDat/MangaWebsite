package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.User;
import com.example.MangaWebsite.Repository.IUserRepository;
import com.example.MangaWebsite.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("username");
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model, HttpSession session) {
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            session.setAttribute("username", existingUser.getUsername());
            return "redirect:/";
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "login";
        }
    }



    @GetMapping("/Register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "Register";
    }
    @PostMapping("/Register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(error -> model.addAttribute(error.getField() + "_error",error.getDefaultMessage()));
            return "Register";
        }
        userService.save(user);
        return "redirect:/login";
    }
}
