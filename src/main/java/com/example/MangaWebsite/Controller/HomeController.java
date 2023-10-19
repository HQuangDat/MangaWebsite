package com.example.MangaWebsite.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String Index(){
        return "index";
    }

    @GetMapping("/login")
    public String Login(){
        return "login";
    }

    @GetMapping("/Register")
    public String Register(){
        return "Register";
    }

    @GetMapping("/Content")
    public String Content(){
        return "Content";
    }
}
