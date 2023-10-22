package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.CategoryService;
import com.example.MangaWebsite.Service.MangaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/manga")
public class MangaController {
    @Autowired
    private MangaService mangaService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/manga/{id}")
    public Truyen get(@PathVariable char id)
    {
        return mangaService.getTruyenById(id);
    }

    //Them truyen
    @GetMapping("/add")
    public String addTruyenForm(Model model){
        model.addAttribute("truyen",new Truyen());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "manga/add";
    }

    @PostMapping("/add")
    public String addTruyen(@ModelAttribute("product") Truyen truyen){
        mangaService.addTruyen(truyen);
        return "redirect:/manga";
    }
    //Xoa truyen
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable char id)
    {
        mangaService.deleteTruyen(id);
    }

    //edit truyen
    //Lấy thông tin của một manga dựa vào ID
    @GetMapping("/edit/{id}")
    public String editTruyenForm(@PathVariable("id") Character id, Model model) {
        Optional<Truyen> editTruyen = Optional.ofNullable(mangaService.getTruyenById(id));
        if (editTruyen.isPresent()) {
            Truyen truyen = editTruyen.get();
            model.addAttribute("truyen", truyen);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("selectedCategoryId", truyen.getMaTheLoai().getMaTheLoai());
            return "manga/edit";
        } else {
            return "not-found";
        }
    }


    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("truyen") Truyen truyen) {
        mangaService.updateTruyen(truyen);
        return "redirect:/manga";
    }
}
