package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.Category;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private TruyenService truyenService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TrangThaiTruyenService trangThaiTruyenService;

    @GetMapping("/search-results")
    public String searchTruyen(
            @RequestParam(required = false, name = "TenTruyen") String TenTruyen,
            @RequestParam(required = false, name = "trangThaiTruyen") Long trangThaiId,
            @RequestParam(required = false, name = "category") Long theLoaiId,
            @RequestParam(required = false, name = "sapxep") Long sapxepId,
            Model model) {
        if (TenTruyen != null) {
            TenTruyen = URLDecoder.decode(TenTruyen, StandardCharsets.UTF_8);
        }

        if (theLoaiId == null) {
            theLoaiId = null;
        }
        if (trangThaiId == null) {
            trangThaiId = null;
        }

        List<Category> categoryspawn = categoryService.getAllCategories();
        List<TrangThaiTruyen> trangThaiTruyenList = trangThaiTruyenService.getAllTrangThais();

        model.addAttribute("categoryspawn", categoryspawn);
        model.addAttribute("trangthaiList", trangThaiTruyenList);
        // Gọi service để thực hiện tìm kiếm và trả về kết quả

        if (sapxepId == null) {
            List<Truyen> truyenList = truyenService.searchTruyen(TenTruyen, trangThaiId, theLoaiId);
            model.addAttribute("truyenList", truyenList);
        }
        else if (sapxepId == 1) {
            List<Truyen> truyenList = truyenService.searchTruyen(TenTruyen, trangThaiId, theLoaiId);
            List<Truyen> DaSapXep = truyenService.getAllTruyenMoiCapNhat(truyenList);
            model.addAttribute("truyenList", DaSapXep);

        }else if (sapxepId ==2 )
        {
            List<Truyen> truyenList = truyenService.searchTruyen(TenTruyen, trangThaiId, theLoaiId);
            List<Truyen> DaSapXep = truyenService.getAllTruyenMoiDangList(truyenList);
            model.addAttribute("truyenList", DaSapXep);

        }
        return "/search";
    }
}
