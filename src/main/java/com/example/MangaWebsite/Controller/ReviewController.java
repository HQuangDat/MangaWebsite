package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Review;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private TruyenService truyenService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ChuongService chuongService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @GetMapping("/review")
    public String reviewform(Model model){

        List<User> userList = userService.getAllUsers();
        List<Truyen> truyenList = truyenService.getAllTruyens();
        List<Review> reviewList = reviewService.getAllReviews();
        model.addAttribute("truyenList", truyenList);
        model.addAttribute("userList",userList);
        model.addAttribute("reviewList",reviewList);
        return "Review";}


    @PostMapping("/review")
    public String reviewtruyen( @ModelAttribute("review") @Valid Review review,
                                @RequestParam("reviewcontent") String reviewcontent,
                               @RequestParam("link") String link,
                               Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String currentUsername = userDetails.getUsername();
        Long currentUserId = userService.getUserIdByUsername(currentUsername);
        User currentUser = userService.getUserbyId(currentUserId);
        review.setUser(currentUser);
        review.setNoiDung(reviewcontent);
        review.setNgayDang(LocalDateTime.now());
        review.setTruyen_url(link);
        reviewService.update(review);

        return "Review";}


}
