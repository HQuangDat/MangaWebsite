package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.Category;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.TrangThaiTruyen;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.CategoryService;
import com.example.MangaWebsite.Service.ChuongService;
import com.example.MangaWebsite.Service.TrangThaiTruyenService;
import com.example.MangaWebsite.Service.TruyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Controller
public class CategoryController {

    @Autowired
    private TruyenService truyenService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ChuongService chuongService;
    @Autowired
    private TrangThaiTruyenService trangThaiTruyenService;

    @GetMapping("/category")
    public String categoryview(Model model){

        List<Truyen> truyenList = truyenService.getAllTruyens();
        List<Category> categoryspawn = categoryService.getAllCategories();
        List<TrangThaiTruyen> trangThaiTruyenList = trangThaiTruyenService.getAllTrangThais();
        model.addAttribute("truyenList", truyenList);
        model.addAttribute("categoryspawn", categoryspawn);
        model.addAttribute("trangthaiList", trangThaiTruyenList);
        return "/category";}



}
