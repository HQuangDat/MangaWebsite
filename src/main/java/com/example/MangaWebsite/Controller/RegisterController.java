package com.example.MangaWebsite.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {
    @GetMapping("/Register")
    public String Register(){
        return "Register";
    }

}
