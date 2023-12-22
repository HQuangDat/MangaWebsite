package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.CustomUserDetail;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.*;
import com.example.MangaWebsite.Service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {
    @Autowired
    private TruyenService truyenService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChuongService chuongService;
    @Autowired
    private AnhService anhService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private ChuongUserService chuongUserService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/truyen/{id_truyen}")
    public String detail(@PathVariable("id_truyen") Long id_truyen, Model model, Authentication authentication) {
        Optional<Truyen> detailTruyen = Optional.ofNullable(truyenService.getTruyenById(id_truyen));
        if (detailTruyen.isPresent()) {
            Truyen truyen = detailTruyen.get();
            model.addAttribute("truyen", truyen);
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof CustomUserDetail) {
                    CustomUserDetail customUserDetail = (CustomUserDetail) principal;
                    boolean userLikedTruyen = likeService.userLikedTruyen(truyen, customUserDetail.getUser());
                    model.addAttribute("userLikedTruyen", userLikedTruyen);
                    List<Chuong> ChuongDaCheck = chuongService.getChuongsWithUserStatus(id_truyen, customUserDetail.getUser());
                    model.addAttribute("ChuongDaChecks", ChuongDaCheck);
                    Chuong firstChuong = chuongService.getFirstChuongOfTruyen(id_truyen);
                   model.addAttribute("firstChuong", firstChuong);
                    return "/truyenDetail";
                }
            } else {
                List<Comment> commentList = commentService.getAllComment(id_truyen);
                model.addAttribute("commentLists", commentList);
                Chuong firstChuong = chuongService.getFirstChuongOfTruyen(id_truyen);
                model.addAttribute("firstChuong", firstChuong);
                List<Chuong> chuongList = chuongService.getChuongsByTruyenId(id_truyen);
                model.addAttribute("ChuongDaChecks", chuongList);
                return "/truyenDetail";
            }
            return "redirect:/";
        } else
            return "redirect:/";
    }

    @GetMapping("truyen/{id_truyen}/chuong/{id_chuong}")
    public String Chuong_content(@PathVariable("id_truyen") Long id_truyen,
                                 @PathVariable("id_chuong") Long id_chuong, Model model) {
        Truyen truyen = truyenService.getTruyenById(id_truyen);
        List<Chuong> chuongList = chuongService.getChuongsByTruyenId(id_truyen);
        List<Anh> anhList = anhService.getAnhsByChuongId(id_chuong);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        truyenService.updateTruyenLuotXem(id_truyen);
        if (chuongService.getChuongById(id_chuong).isLocked()) {
            if (principal instanceof CustomUserDetail) {
                CustomUserDetail customUserDetail = (CustomUserDetail) principal;
                List<Chuong> ChuongDaCheck = chuongService.getChuongsWithUserStatus(id_truyen, customUserDetail.getUser());
                if (truyen == null) {
                    if (!chuongService.isChuongBelongsToTruyen(id_truyen, id_chuong)) {
                        return "redirect:/truyen/{id_truyen}?error";
                    }
                }
                if (ChuongDaCheck.stream().noneMatch(chuong -> chuong.getId().equals(id_chuong))) {
                    return "redirect:/truyen/{id_truyen}?errorLockedChuong";
                }
            }
        }
        if(!chuongService.getChuongById(id_chuong).isLocked()){
            // Lấy index của chương hiện tại
            int currentIndex = -1;
            for (int i = 0; i < chuongList.size(); i++) {
                if (chuongList.get(i).getId().equals(id_chuong)) {
                    currentIndex = i;
                    break;
                }
            }



            Chuong chuongHienTai = chuongList.get(currentIndex);
            Chuong chuongTruoc = (currentIndex > 0) ? chuongList.get(currentIndex - 1) : null;
            Chuong chuongSau = (currentIndex < chuongList.size() - 1) ? chuongList.get(currentIndex + 1) : null;

            model.addAttribute("anh", anhList);
            model.addAttribute("truyen", truyen);
            model.addAttribute("chuong", chuongList);
            model.addAttribute("chuongHienTai", chuongHienTai);
            model.addAttribute("chuongTruoc", chuongTruoc);
            model.addAttribute("chuongSau", chuongSau);
            return "/chuongContent";
        }
        return "redirect:/truyen/{id_truyen}?error";
    }

    @PostMapping("/like/{truyenId}")
    public String likeTruyen(@PathVariable Long truyenId, Model model, Authentication authentication) {
        Optional<Truyen> truyenOptional = truyenService.findById(truyenId);

        if (truyenOptional.isPresent()) {
            Truyen truyen = truyenOptional.get();
            // Nhận thông tin người dùng hiện tại từ SecurityContext
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String currentUsername = userDetails.getUsername();

            // Bạn có thể sử dụng username để lấy ID của người dùng từ service
            Long currentUserId = userService.getUserIdByUsername(currentUsername);

            // Đặt giá trị cho trường user trong đối tượng Truyen
            User currentUser = userService.getUserbyId(currentUserId);
            likeService.toggleLike(truyen, currentUser);
            truyenService.updateTruyen(truyen);
        }
        model.addAttribute("likeService", likeService);

        return "redirect:/truyen/{truyenId}";
    }

    // Trong phương thức xử lý khi người dùng muốn mua một chương
    @Transactional
    @PostMapping("/mua-chuong/{id_truyen}/{id_chuong}")
    public String muaChuong(Model model, @PathVariable Long id_chuong) {

        Chuong chuong = chuongService.getChuongById(id_chuong);
        User userlaytien = userService.getUserbyId(chuong.getUser().getId());
        // Nhận thông tin người dùng hiện tại từ SecurityContext
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = userDetails.getUsername();
        Long currentUserId = userService.getUserIdByUsername(currentUsername);
        User currentUser = userService.getUserbyId(currentUserId);
        if (!chuongUserService.userBoughtChuong(chuong, currentUser)) {
            if (currentUser.getSoDu() > chuong.getGiaTien()) {
                chuongUserService.updateChuong_User(chuong, currentUser);
                userlaytien.setSoDu(userlaytien.getSoDu() + chuong.getGiaTien());
                userService.updateUser(userlaytien);
            } else
                return "redirect:/truyen/{id_truyen}?errorkhongdutien";
        } else {
            return "redirect:/truyen/{id_truyen}";
        }
        return "redirect:/truyen/{id_truyen}";
    }

}
