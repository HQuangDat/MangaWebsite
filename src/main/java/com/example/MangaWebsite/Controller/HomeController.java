package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Service.CategoryService;
import com.example.MangaWebsite.Service.ChuongService;
import com.example.MangaWebsite.Service.TruyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.MangaWebsite.Model.*;
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
    public String Index(Model model){
        // Lấy danh sách tất cả truyện
        List<Truyen> truyenList = truyenService.getAllTruyens();
        List<Chuong> chuongList = chuongService.getAllChuongs();

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        // Lọc ra danh sách chương mới cập nhật trong vòng 1 ngày và sắp xếp theo ngày đăng giảm dần
        List<Chuong> chuongMoiCapNhatList = chuongList.stream()
                .filter(chuong -> chuong.getNgayDang().isAfter(oneDayAgo))
                .sorted(Comparator.comparing(Chuong::getNgayDang).reversed())
                .collect(Collectors.toList());
        // Lọc ra danh sách truyện mà có chương mới cập nhật
        List<Truyen> truyenMoiCapNhatList = truyenList.stream()
                .filter(truyen -> chuongMoiCapNhatList.stream()
                        .anyMatch(chuong -> Objects.equals(chuong.getTruyen().getId(), truyen.getId())))
                .collect(Collectors.toList());
        List<Truyen> topTruyenList = truyenMoiCapNhatList.stream().limit(6).collect(Collectors.toList());
         model.addAttribute("topTruyenList", topTruyenList);
        // Sắp xếp truyện theo ngày cập nhật giảm dần------------------------------------------------------------------------
        List<Truyen> truyenList2 = truyenService.getAllTruyens();
        truyenList2.sort(Comparator.comparing(Truyen::getNgayDang).reversed());
            List<Truyen> topTruyenMoiDangList = truyenList2.stream().limit(6).collect(Collectors.toList());
            model.addAttribute("topTruyenMoiDangList", topTruyenMoiDangList);

        // Lấy danh sách top truyện theo số lượt view (topViewsList)--------------------------------------------------------
        List<Truyen> topViewsList = truyenService.getAllTruyens();
        topViewsList.sort(Comparator.comparingInt(Truyen::getSoView).reversed());
        List<Truyen> TopViewsList = topViewsList.stream().limit(5).collect(Collectors.toList());

        model.addAttribute("topViewsLists", TopViewsList);
   // Lấy danh sách top truyện theo số lượt like (topLikeList)--------------------------------------------------------
        List<Truyen> topLikeList = truyenService.getAllTruyens();
        topLikeList.sort(Comparator.comparingInt(Truyen::getSoView).reversed());
        List<Truyen> TopLikeList = topLikeList.stream().limit(6).collect(Collectors.toList());

        model.addAttribute("topLikeLists", TopLikeList);
        return "index";
    }




}
