package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.CategoryService;
import com.example.MangaWebsite.Service.TruyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/truyen")
public class MangaController {
    @Autowired
    private TruyenService truyenService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
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
            return new ResponseEntity<>("Invalid ID format", HttpStatus.BAD_REQUEST);
        }
    }


    // Thêm truyện
    @GetMapping("/add")
    public String addTruyenForm(Model model) {
        model.addAttribute("truyen", new Truyen());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "truyen/add";
    }
    @PostMapping("/add")
    public ResponseEntity<String> addTruyen(@ModelAttribute Truyen truyen, @RequestParam("avatar") MultipartFile avatar) {
        try {
            // Lưu file avatar vào đối tượng truyen
            truyen.setAvatar(avatar.getBytes());

            // Thực hiện lưu trữ dữ liệu vào cơ sở dữ liệu bằng MangaService
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
