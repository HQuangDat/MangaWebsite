package com.example.MangaWebsite.Controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
@Controller
public class AdminController {
    @GetMapping("/admin")
    public String Admin() {return "Admin/Admin";}
}
