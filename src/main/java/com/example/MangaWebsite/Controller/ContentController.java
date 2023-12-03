package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.ChuongService;
import com.example.MangaWebsite.Service.TruyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {
    @Autowired
    private TruyenService truyenService;

    @Autowired
    private ChuongService chuongService;

    @GetMapping("/Content")
    public String Content(){
        return "Content";
    }

    //edit
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        Optional<Truyen> detailTruyen = Optional.ofNullable(truyenService.getTruyenById(id));
        List<Chuong> chuongs = chuongService.getAllChuongs();
        model.addAttribute("chuongs", chuongs);
        if (detailTruyen.isPresent()) {
            Truyen truyen = detailTruyen.get();
            model.addAttribute("truyen", truyen);
            return "/truyenDetail";
        }
        else
            return "redirect:/error";
    }
}
