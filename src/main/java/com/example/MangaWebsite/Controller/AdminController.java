package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.Role;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.*;
import com.example.MangaWebsite.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Set;


@Configuration
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TruyenService truyenService;
    @Autowired
    private ChuongService chuongService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ChuongUserService chuongUserService;


    @GetMapping("")
    public String Admin(Model model) {
        List<ChartData> chartData = truyenService.getChartDataForLastWeek();

        // Đưa dữ liệu vào model để hiển thị trong template
        model.addAttribute("chartData", chartData);
        List<ChartData> chartDataChuong = chuongService.getChartDataForLastWeekChuong();
        model.addAttribute("chartDataChuong", chartDataChuong);
        return "/Admin/Admin";}



    //--------------------------------THỂ Loại-------------------------------
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
    public String themTheLoaiForm(@ModelAttribute("newCategory") Category category) {
        // Thực hiện xử lý để thêm thể loại vào cơ sở dữ liệu
        categoryService.saveCategory(category);
        // Sau khi thêm, bạn có thể chuyển hướng hoặc làm bất kỳ điều gì khác
        return  "redirect:/admin/them-the-loai";
    }
    @PostMapping("/delete-the-loai/{Idcategory}")
    public String xoatheLoai(@PathVariable Long Idcategory ){
        Category category = categoryService.getCategoryById(Idcategory);
        if (category != null) {
            truyenService.ChangeCategoryAllTruyensToNewCategory(Idcategory,categoryService.getCategoryById(1L));
            categoryService.deleteCategory(Idcategory);
        }
        return  "redirect:/admin/them-the-loai";
    }
    //-------------------------------------------------QtV-------------------------------

    @GetMapping("/qtv")
    public String listUsers(Model model) {
        List<User> userList = userService.getUsersBySingleRole(1L);

                // Gửi danh sách user và số trang tới view
                model.addAttribute("users", userList);
        return "/Admin/quan-ly-user";
    }
    @PostMapping("qtv/{userId}/delete")
    public String deleteUser(@PathVariable Long userId) {
            // Lấy thông tin người dùng
            User user = userService.getUserbyId(userId);

            // Kiểm tra xem người dùng có tồn tại hay không
            if (user != null) {
                truyenService.ChangeUserAllTruyensToNewUser(userId,userService.getUserbyId(1L));
                userService.deleteUser(userId);
            }
        return "redirect:/admin/qtv";
    }
    //-------------------------------------------------CTV-------------------------------
    @GetMapping("/ctv")
    public String ListCtv(Model model) {
        List<User> userList = userService.getAllUsersWithRoles(3L);
        // Gửi danh sách user và số trang tới view
        model.addAttribute("users", userList);
        return "/Admin/cong-tac-vien";}
    //-------------------------------------------------Nâng & Giảm ROle -------------------------------
    @PostMapping("qtv/{userId}/upRoles")
    public String upRolesUser(@PathVariable Long userId) {
        // Lấy thông tin người dùng
        User user = userService.getUserbyId(userId);

        // Kiểm tra xem người dùng có tồn tại hay không
        if (user != null) {
            userService.updateRoleCTVForUser(userId);
        }
        return "redirect:/admin/qtv";
    }
    @PostMapping("ctv/{userId}/deRoles")
    public String DeRolesCTV(@PathVariable Long userId) {
        // Lấy thông tin người dùng
        User user = userService.getUserbyId(userId);
        if (user != null) {
            userService.deleteRoleAssignment(userId,3L);
        }
        return "redirect:/admin/ctv";
    }

    //-------------------------------------------------NapTien-------------------------------
@GetMapping("/nap-tien")
public String naptienform(Model model) {

    List<User> userList = userService.getAllUsers();
    // Gửi danh sách user và số trang tới view
    model.addAttribute("users", userList);
    model.addAttribute("userInput", new UserInput());

    return "Admin/nap-tien";
}
    @PostMapping("/nap-tien")
    public String naptienSubmit(@ModelAttribute UserInput userInput) {
        if(userInput.getId() ==null)
            return "redirect:/admin/nap-tien?error";
        Long userId = userInput.getId();
        Double amount = userInput.getAmount();
        User user = userService.getUserbyId(userId);
        if (amount > 0) {
            user.setSoDu(user.getSoDu() + amount);
        } else if (amount < 0 && user.getSoDu() >= Math.abs(amount)) {
            user.setSoDu(user.getSoDu() + amount);
        } else {
                return "redirect:/admin/nap-tien?error";
        }
        userService.updateUser(user);
        return "redirect:/admin/nap-tien?success";
    }
    //-----------------------------Lich su mua chuong---------------------------------------------
    @GetMapping("/lich-su")
    public String listMuaChuong(Model model) {
        List<Chuong_User> chuongUserList = chuongUserService.getAllChuongUser();
        // Gửi danh sách user và số trang tới view
        model.addAttribute("chuongUserlist", chuongUserList);
        return "/Admin/lich-su";
    }
}
