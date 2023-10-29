package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Model.User;
import com.example.MangaWebsite.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){return "/login";}

    @GetMapping("/Register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "Register";
    }
    @PostMapping("/Register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            bindingResult.getFieldErrors().forEach(error -> model.addAttribute(error.getField() + "_error",error.getDefaultMessage()));
            return "Register";
        }
        userService.save(user);
        return "redirect:/login";
    }
}
