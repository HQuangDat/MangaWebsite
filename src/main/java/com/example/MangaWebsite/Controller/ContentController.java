package com.example.MangaWebsite.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    @GetMapping("/Content")
    public String Content(){
        return "Content";
    }
}
