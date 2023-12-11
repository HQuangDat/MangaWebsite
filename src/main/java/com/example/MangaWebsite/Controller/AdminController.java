package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.Role;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Category;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.CategoryService;
import com.example.MangaWebsite.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;


@Configuration
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @Autowired
    private CategoryService categoryService;



    @GetMapping("")
    public String Admin() {return "/Admin/Admin";}
    @GetMapping("/them-the-loai")
    public String themTheLoaiForm(Model model) {
        // Truyền danh sách thể loại vào mô hình để hiển thị
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        // Truyền một đối tượng mới của Category để bind với form thêm mới
        model.addAttribute("newCategory", new Category());

        return "Admin/them-the-loai";
    }

    @PostMapping("/them-the-loai")
    public String themTheLoaiForm(@ModelAttribute("category") Category category) {
        // Thực hiện xử lý để thêm thể loại vào cơ sở dữ liệu
        categoryService.saveCategory(category);
        // Sau khi thêm, bạn có thể chuyển hướng hoặc làm bất kỳ điều gì khác
        return  "redirect:/admin/them-the-loai";
    }
    @GetMapping("/ctv")
    public String ctv() {return "/Admin/cong-tac-vien";}
    @GetMapping("/qtv")
    public String listUsers(@RequestParam(name = "page", defaultValue = "1") int page, Model model) {
        // Số lượng user trên mỗi trang
        int pageSize = 20;

        // Tính chỉ số bắt đầu của user trong danh sách
        int startIndex = (page - 1) * pageSize;

        // Lấy danh sách user từ startIndex đến startIndex + pageSize
        List<User> userList = userService.getAllUsersWithRoles(1L);// Lấy danh sách user từ service hoặc repository của bạn

                // Gửi danh sách user và số trang tới view
                model.addAttribute("users", userList);

        // Gửi số trang hiện tại tới view để highlight nút phân trang
        model.addAttribute("currentPage", page);

        return "/Admin/quan-ly-user";
    }
    @PostMapping("qtv/nap-tien")
    public String recharge(@RequestParam("userId") Long userId, @RequestParam("amount") Double amount) {
        // Xử lý nạp tiền và cập nhật cơ sở dữ liệu ở đây
        userService.rechargeUser(userId, amount);
        // Sau khi xử lý xong, chuyển hướng hoặc trả về trang mong muốn
        return "redirect:/admin/qtv";
    }



    @GetMapping("/thanhvien")
    public String thanhvien() {return "/Admin/thanh-vien";}

}
