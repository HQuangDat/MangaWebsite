package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.MangaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manga")
public class MangaController {
    @Autowired
    private MangaService mangaService;

    @GetMapping("/manga/${id}")
    public Truyen get(@PathVariable char id)
    {
        return mangaService.get(id);
    }

    //Xoa truyen
    @DeleteMapping("/manga/${id}")
    public void delete(@PathVariable char id)
    {
        mangaService.remove(id);
    }
}
