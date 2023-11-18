package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.Category;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.CategoryService;
import com.example.MangaWebsite.Service.TruyenService;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("admin/truyen")
public class MangaController {
    @Autowired
    private TruyenService truyenService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("List/{id}")
    public ResponseEntity<?> getTruyen(@PathVariable String id) {
        try {
            long truyenId = Long.parseLong(id);
            Truyen truyen = truyenService.getTruyenById(truyenId);
            if (truyen != null) {
                return new ResponseEntity<>(truyen, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Truyen not found", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            // Xử lý ngoại lệ nếu id không thể chuyển đổi thành kiểu long
            return new ResponseEntity<>("Lỗi này này", HttpStatus.BAD_REQUEST);
        }
    }

// Hiển thị trang thêm truyện (GET)
    @GetMapping("/add")
    public String   AddTruyenForm(Model model) {
        // Tạo một đối tượng Truyen để binding với form
        Truyen truyen = new Truyen();

        // Truyền đối tượng Truyen và danh sách các danh mục vào model
        model.addAttribute("truyen", truyen);
        model.addAttribute("categories", categoryService.getAllCategories());

        // Trả về tên view (thường là tên của trang thêm truyện)
        return "truyen/add";
    }
    @PostMapping("/add")
    public ResponseEntity<String> addTruyen(@ModelAttribute("truyen") @Valid Truyen truyen,
                                            @RequestParam("avatar") MultipartFile avatarFile,
                                            BindingResult result,
                                            Model model) {
        if (result.hasErrors()) {
            // Xử lý lỗi kiểm tra hợp lệ
            return new ResponseEntity<>("Có lỗi kiểm tra hợp lệ trong biểu mẫu", HttpStatus.BAD_REQUEST);
        }

        try {
            // Kiểm tra rằng tệp avatar không trống
            if (avatarFile.isEmpty()) {
                return new ResponseEntity<>("Yêu cầu tệp avatar", HttpStatus.BAD_REQUEST);
            }

            long maxSize = 10 * 1024 * 1024; // 10MB
            if (avatarFile.getSize() > maxSize) {
                return new ResponseEntity<>("Kích thước tệp quá lớn, vui lòng chọn tệp nhỏ hơn " + maxSize + " bytes", HttpStatus.BAD_REQUEST);
            }

// Kiểm tra loại nội dung
            String allowedContentType = "image/*";
            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                return new ResponseEntity<>("Loại tệp không được hỗ trợ, vui lòng chọn tệp hình ảnh", HttpStatus.BAD_REQUEST);
            }

// Lưu tên tệp và đường dẫn thực tế
            String fileName = UUID.randomUUID().toString() + "_" + avatarFile.getOriginalFilename();
            Path filePath = Paths.get("C://Users//Admin//Desktop//MangaWebsite//imgtaive", fileName);

            try {
                // Đảm bảo thư mục "/imgtaive" đã tồn tại
                if (!Files.exists(filePath.getParent())) {
                    Files.createDirectories(filePath.getParent());
                }

                // Ghi tệp vào đường dẫn
                Files.write(filePath, avatarFile.getBytes());
            } catch (IOException e) {
                return new ResponseEntity<>("Lỗi khi lưu tệp: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Lưu thông tin vào đối tượng truyen
            truyen.setAvatarFileName(fileName);
            truyen.setAvatarFilePath(filePath.toString());

            // Nhận danh mục được chọn dựa trên ID từ biểu mẫu
            if (truyen.getCategory() == null || truyen.getCategory().getId() == null) {
                return new ResponseEntity<>("Danh mục không hợp lệ", HttpStatus.BAD_REQUEST);
            }
            Category selectedCategory = categoryService.getCategoryById(truyen.getCategory().getId());
            if (selectedCategory == null) {
                return new ResponseEntity<>("Danh mục không tồn tại", HttpStatus.BAD_REQUEST);
            }
            // Đặt danh mục trong đối tượng truyen
            truyen.setCategory(selectedCategory);
            truyen.setNgayDang(LocalDateTime.now());
            // Lưu dữ liệu vào cơ sở dữ liệu bằng MangaService
            truyenService.addTruyen(truyen);

            return new ResponseEntity<>("Truyện đã được thêm thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi thêm Truyện: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    // Xóa truyện
    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteTruyen(@PathVariable long id) {
        truyenService.deleteTruyen(id);
        return new ResponseEntity<>("Truyện đã được xóa thành công", HttpStatus.OK);
    }

    // Chỉnh sửa truyện
    // Lấy thông tin của một manga dựa vào ID
    @GetMapping("/edit/{id}")
    public String editTruyenForm(@PathVariable("id") Long id, Model model) {
        Optional<Truyen> editTruyen = Optional.ofNullable(truyenService.getTruyenById(id));
        if (editTruyen.isPresent()) {
            Truyen truyen = editTruyen.get();
            model.addAttribute("truyen", truyen);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("selectedCategoryId", truyen.getCategory().getId());
            return "truyen/edit";
        } else {
            return "not-found";
        }
    }

    @PostMapping("/edit")
    public String editTruyen(@ModelAttribute("truyen") Truyen truyen) {
        truyenService.updateTruyen(truyen);
        return "redirect:truyen/list"; // Sửa đường dẫn chuyển hướng
    }


    @GetMapping("/list")
    public String listTruyens(Model model) {
        List<Truyen> truyens = truyenService.getAllTruyens();
        model.addAttribute("truyens", truyens);
        return "truyen/list";
    }
    @GetMapping("/list/{userId}")
    public String listTruyensByUserId(@PathVariable Long userId, Model model) {
        List<Truyen> truyens = truyenService.getTruyensByUserId(userId);
        model.addAttribute("truyens", truyens);
        return "truyen/list";
    }
}
