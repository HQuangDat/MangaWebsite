package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.CustomUserDetail;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.*;
import com.example.MangaWebsite.Repository.IChuongRepository;
import com.example.MangaWebsite.Service.*;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("admin/truyen")
public class MangaController {
    @Autowired
    private TruyenService truyenService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChuongService chuongService;
    @Autowired
    private AnhService anhService;
    @Autowired
    private ChuongUserService chuongUserService;

    @GetMapping("List/{id}")
    public String getTruyen(@PathVariable String id) {
        try {
            long truyenId = Long.parseLong(id);
            Truyen truyen = truyenService.getTruyenById(truyenId);
            if (truyen != null) {
                return "redirect:/admin/List/{id}";
            } else {
                return "redirect:/admin/truyen/List/{id}?error";
            }
        } catch (NumberFormatException e) {
            return "redirect:/admin/truyen/List/{id}?error";
        }
    }


    @GetMapping("/add")
    public String AddTruyenForm(Model model) {

        Truyen truyen = new Truyen();


        model.addAttribute("truyen", truyen);
        model.addAttribute("categories", categoryService.getAllCategories());


        return "truyen/add";
    }

    @PostMapping("/add")
    public String addTruyen(@ModelAttribute("truyen") @Valid Truyen truyen,
                            @RequestParam("avatar") MultipartFile avatarFile,
                            BindingResult result,
                            Model model,
                            Authentication authentication
    ) {

        if (result.hasErrors()) {

            return "redirect:/add?error";
        }

        try {

            if (avatarFile.isEmpty()) {
                return "redirect:/admin/truyen/add?error";
            }

            long maxSize = 10 * 1024 * 1024; // 10MB
            if (avatarFile.getSize() > maxSize) {
                return "redirect:/admin/truyen/add?error";
            }


            String allowedContentType = "image/*";
            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                return "redirect:/admin/truyen/add?error";
            }
            if (avatarFile.getOriginalFilename() == null) {

                return "redirect:/admin/truyen/add?error";
            }

            String fileName = avatarFile.getOriginalFilename();
            String sanitizedFileName = sanitizeFileName(fileName);

            Path directoryPath = Paths.get("E:/GCWT2", sanitizeFileName(truyen.getTenTruyen()));


            Path filePath = directoryPath.resolve(sanitizedFileName);
            String filepath = sanitizeFileName(truyen.getTenTruyen()) + "/" + sanitizedFileName;

            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Files.write(filePath, avatarFile.getBytes());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();

            Long currentUserId = userService.getUserIdByUsername(currentUsername);

            User currentUser = userService.getUserbyId(currentUserId);
            truyen.setUser(currentUser);

            if (truyen.getCategory() == null || truyen.getCategory().getId() == null) {
                return "redirect:/admin/truyen/add?error";
            }
            Category selectedCategory = categoryService.getCategoryById(truyen.getCategory().getId());
            if (selectedCategory == null) {
                return "redirect:/admin/truyen/add?error";
            }


            truyen.setAvatarFileName(filepath);
  truyen.setCategory(selectedCategory);
            truyen.setNgayDang(LocalDateTime.now());

            truyenService.addTruyen(truyen);

            return "redirect:/admin/truyen/add?success";
        } catch (Exception e) {
            return "redirect:/admin/truyen/add?error";
        }
    }

    private String sanitizeFileName(String fileName) {
        // Chuyển đổi tên file thành không dấu và thay thế khoảng trắng bằng dấu _
        String fileNameWithoutAccent = convertToAscii(fileName);
        return fileNameWithoutAccent.replaceAll("\\s", "_");
    }

    private String convertToAscii(String input) {
        return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

//-------------------------------  Xóa truyện ----------------------------------

    //

    @PostMapping("/{truyenId}/delete")
    public String deleteTruyen(@PathVariable Long truyenId,
                               Authentication authentication) {
        if (truyenId != null) {
            truyenService.deleteTruyen(truyenId);
            return "redirect:../list";
        } else return "redirect:../list?error";
    }

    @PostMapping("/{truyenId}/{chuongId}/delete")
    public String deleteChuong(@PathVariable Long truyenId,
                               @PathVariable Long chuongId,
                               Authentication authentication) {
        if (chuongId != null) {
            Truyen truyen = truyenService.getTruyenById(truyenId);
            chuongUserService.DeleteAllByChuongId(chuongService.getChuongById(chuongId));
            truyen.setSoChuong(truyen.getSoChuong() - 1);
            truyenService.updateTruyen(truyen);
            chuongService.deleteChuong(chuongId);
            return "redirect:../list-chuong";
        } else return "redirect:../list-chuong?error";
    }
    //-------------------------------  Chỉnh sửa truyện ----------------------------------

    @GetMapping("/{truyenId}/edit")
    public String editTruyenForm(@PathVariable Long truyenId, Model model) {

        Truyen EditTruyen = truyenService.getTruyenById(truyenId);
        model.addAttribute("EditTruyen", EditTruyen);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "truyen/edit-truyen";
    }

    @PostMapping("/{truyenId}/edit")
    public String editTruyen(
            @PathVariable Long truyenId,
            @ModelAttribute("truyen") Truyen truyen,
            @RequestParam("avatar") MultipartFile avatarFile,
            BindingResult result,
            Model model,
            Authentication authentication
    ) {

        if (result.hasErrors()) {
            return "redirect:/admin/truyen/{truyenId}/edit?error";
        }

        try {
            if (avatarFile.isEmpty()) {
                return "redirect:/admin/truyen/{truyenId}/edit?error";
            }
            long maxSize = 10 * 1024 * 1024; // 10MB
            if (avatarFile.getSize() > maxSize) {
                return "redirect:/admin/truyen/{truyenId}/edit?error";
            }
            String allowedContentType = "image/*";
            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                return "redirect:/admin/truyen/{truyenId}/edit?error";
            }
            String fileName = avatarFile.getOriginalFilename();
            String sanitizedFileName = sanitizeFileName(fileName);

            Path directoryPath = Paths.get("E:/GCWT2", sanitizeFileName(truyen.getTenTruyen()));
            Path filePath = directoryPath.resolve(sanitizedFileName);
            String SaveURL = sanitizeFileName(truyen.getTenTruyen() + '/' + sanitizedFileName);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Files.write(filePath, avatarFile.getBytes());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            Long currentUserId = userService.getUserIdByUsername(currentUsername);

            User currentUser = userService.getUserbyId(currentUserId);


            if (truyen.getCategory() == null || truyen.getCategory().getId() == null) {
                return "redirect:/admin/truyen/{truyenId}/edit?error";
            }
            Category selectedCategory = categoryService.getCategoryById(truyen.getCategory().getId());
            if (selectedCategory == null) {
                return "redirect:/admin/truyen/{truyenId}/edit?error";
            }
            Truyen truyenEdit = truyenService.getTruyenById(truyenId);
            if (truyenEdit == null) {
                return "redirect:/admin/truyen/{truyenId}/edit?error";
            }
            // Cập nhật thông tin truyện
            truyenEdit.setTenTruyen(truyen.getTenTruyen());
            truyenEdit.setTacGia(truyen.getTacGia());
            truyenEdit.setMoTaNoiDung(truyen.getMoTaNoiDung());
            truyenEdit.setAvatarFileName(SaveURL);
            truyenEdit.setUser(currentUser);
            truyenEdit.setCategory(truyen.getCategory());
            truyenService.updateTruyen(truyenEdit);

            return "redirect:/admin/truyen/{truyenId}/edit?error";
        } catch (Exception e) {
            return "redirect:/admin/truyen/{truyenId}/edit?error";
        }
    }


    @GetMapping("/list")
    public String listTruyens(Model model) {
        List<Truyen> truyens = truyenService.getAllTruyens();
        model.addAttribute("truyens", truyens);
        return "truyen/list";
    }

    //<----------------------------------------------------------------------------------------->
    //Chương và Ảnh
    @PostMapping("/add-chuong")
    public String createChuong(
            @ModelAttribute("chuong") @Valid Chuong chuong,
            @RequestParam("anhChuong") MultipartFile[] anhFiles,
            BindingResult result,
            Model model,
            Authentication authentication
    ) {
        try {
            if (chuong.getTruyen() == null || chuong.getTruyen().getId() == null) {
                return "redirect:/admin/truyen/add-chuong?error";
            }
            Truyen selectedTruyen = truyenService.getTruyenById(chuong.getTruyen().getId());
            if (selectedTruyen == null) {
                return "redirect:/admin/truyen/add-chuong?error";
            }
            chuong.setTruyen(selectedTruyen);
            List<Anh> anhList = new ArrayList<>();
            if (chuong.getId() == null) {
                // Lưu chương vào cơ sở dữ liệu
                chuongService.addChuongs(chuong);
            }
            for (MultipartFile image : anhFiles) {
                String imageUrl = saveImageToDatabase(image, chuong.getTruyen().getTenTruyen(), chuong.getTenChuong());
                Anh anh = new Anh();
                anh.setChuong(chuong);
                anh.setDuongDan(imageUrl);
                anhList.add(anh);
            }
            chuong.setAnhList(anhList);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            Long currentUserId = userService.getUserIdByUsername(currentUsername);
            User currentUser = userService.getUserbyId(currentUserId);
            if (currentUser == null) {
                return "redirect:/admin/truyen/add-chuong?error";
            }
            chuong.setUser(currentUser);
            chuong.setNgayDang(LocalDateTime.now());
            for (Anh anh : chuong.getAnhList()) {
                anh.setChuong(chuong);
            }
            if (chuong.getTruyen().isPremium()) {
                if (chuong.getGiaTien() > 0) {
                    chuong.setGiaTien(chuong.getGiaTien());
                    chuong.setLocked(true);
                }
            } else
                chuong.setGiaTien(0);
            chuongService.addChuongs(chuong);
            Truyen truyen = chuong.getTruyen();
            int soLuongChuongHienTai = truyen.getSoChuong();
            truyen.setSoChuong(soLuongChuongHienTai + 1);
            truyenService.addTruyen(truyen);

            return "redirect:/admin/truyen/add-chuong?success";
        } catch (IOException e) {
            return "redirect:/admin/truyen/add-chuong?error";
        }
    }

    @GetMapping("/add-chuong")
    public String showAddChuongForm(Model model) {
        Chuong chuong = new Chuong();
        model.addAttribute("chuong", chuong);
        model.addAttribute("truyen", truyenService.getAllTruyens());

        return "truyen/add-chuong";
    }

    private String saveImageToDatabase(MultipartFile image, String tenTruyen, String tenChuong) throws IOException {
        // Kiểm tra kích thước tệp
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (image.getSize() > maxSize) {
            throw new IOException("Kích thước tệp quá lớn, vui lòng chọn tệp nhỏ hơn " + maxSize + " bytes");
        }

        String allowedContentType = "image/*";
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new IOException("Loại tệp không được hỗ trợ, vui lòng chọn tệp hình ảnh");
        }

        String fileName = image.getOriginalFilename();
        String sanitizedFileName = sanitizeFileName(fileName);

        Path directoryPath = Paths.get("E:/GCWT2", sanitizeFileName(tenTruyen), sanitizeFileName(tenChuong));
        Path filePath = directoryPath.resolve(sanitizedFileName);

        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        try {
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            throw new IOException("Lỗi khi lưu ảnh: " + e.getMessage());
        }
        return sanitizeFileName(tenTruyen) + "/" + sanitizeFileName(tenChuong) + "/" + sanitizedFileName;
    }



    @GetMapping("/{truyenId}/list-chuong")
    public String getTruyenAndChuongs(@PathVariable Long truyenId, Model model) {
        // Kiểm tra xem truyen có tồn tại hay không, nếu không thì trả về 404
        Truyen truyen = truyenService.getTruyenById(truyenId);
        if (truyen == null) {
            return "error/404";  // Hoặc chuyển hướng đến trang lỗi 404
        }

        // Lấy danh sách chương của truyen
        List<Chuong> chuong = chuongService.getChuongsByTruyenId(truyenId);

        // Đưa thông tin truyen và danh sách chuong vào model để hiển thị trên view
        model.addAttribute("truyen", truyen);
        model.addAttribute("chuong", chuong);

        // Trả về tên của view template hiển thị thông tin truyen và danh sách chuong
        return "truyen/list-chuong";
    }

    //-------------------------------Thao tác bật premium Truyen ----------------------------------
    @PostMapping("/{truyenId}/premium")
    public String premiumTruyen(@PathVariable Long truyenId,
                                Authentication authentication) {
        Truyen truyen = truyenService.getTruyenById(truyenId);
        if (truyenId != null) {
            truyenService.togglePremiumStatus(truyenId);
            truyen.setPremium(true);
            return "redirect:../list";
        } else return "redirect:../list?error";
    }
}



