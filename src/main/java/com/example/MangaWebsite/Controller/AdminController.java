package com.example.MangaWebsite.Controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    public String Admin() {return "Admin/Admin";}
    @GetMapping("/chuyenmuc")
    public String chuyenmuc() {return "Admin/chuyen-muc";}
    @GetMapping("/ctv")
    public String ctv() {return "Admin/cong-tac-vien";}
    @GetMapping("/qtv")
    public String qtv() {return "Admin/quan-tri-vien";}

    @GetMapping("/thanhvien")
    public String thanhvien() {return "Admin/thanh-vien";}

}
