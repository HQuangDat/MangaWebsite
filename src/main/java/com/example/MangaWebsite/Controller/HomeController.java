package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.CategoryService;
import com.example.MangaWebsite.Service.ChuongService;
import com.example.MangaWebsite.Service.TruyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private TruyenService truyenService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ChuongService chuongService;

    @GetMapping("/")
    public String Index(Model model) {

        List<Truyen> truyenList = truyenService.getAllTruyens();
        List<Chuong> chuongList = chuongService.getAllChuongs();

        chuongList.sort(Comparator.comparing(Chuong::getNgayDang).reversed());

        List<Chuong>chuongMoiCapNhatList = chuongList.stream().limit(6).collect(Collectors.toList());

        List<Truyen> truyenMoiCapNhatList = truyenList.stream()
                .filter(truyen -> chuongMoiCapNhatList.stream()
                        .anyMatch(chuong -> Objects.equals(chuong.getTruyen().getId(), truyen.getId())))
                .collect(Collectors.toList());
        List<Truyen> topTruyenList = truyenMoiCapNhatList.stream().limit(6).collect(Collectors.toList());
        model.addAttribute("topTruyenList", topTruyenList);

        // Lấy danh sách top truyện mới đăng
        List<Truyen> topTruyenMoiDangList = truyenService.getTopTruyenMoiDangList();
        model.addAttribute("topTruyenMoiDangList", topTruyenMoiDangList);

        // Lấy danh sách top truyện theo số lượt view (topViewsList)--------------------------------------------------------
        List<Truyen> topViewsList = truyenService.getAllTruyens();
       List<Truyen> topTruyenNhieuView = truyenService.getTopTruyenNhieuViewList();
        model.addAttribute("topViewsLists", topTruyenNhieuView);

        // Lấy danh sách top truyện theo số lượt like (topLikeList)--------------------------------------------------------
        List<Truyen> topLikeList = truyenService.getAllTruyens();
        List<Truyen> TopLikeList = truyenService.getTopTruyenNhieuLikeList();
        model.addAttribute("topLikeLists", TopLikeList);
        return "index";
    }


}
