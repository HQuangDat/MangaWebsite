package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.TaiKhoan;
import com.example.MangaWebsite.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/account/{id}")
    public TaiKhoan get(@PathVariable char id){return accountService.get(id);}
}
