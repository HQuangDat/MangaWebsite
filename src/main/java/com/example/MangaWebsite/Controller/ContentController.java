package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Anh;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Service.AnhService;
import com.example.MangaWebsite.Service.ChuongService;
import com.example.MangaWebsite.Service.TruyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {
    @Autowired
    private TruyenService truyenService;

    @Autowired
    private ChuongService chuongService;
    @Autowired
    private AnhService anhService;

//    Chuong test
//    @GetMapping("/chuong/{id}")
//    public String Content(@PathVariable("id") Long id, Model model){
//        Optional<Chuong> chuongOptional = Optional.ofNullable(chuongService.getChuongById(id));
//        List<Chuong> chuongs = chuongService.getAllChuongs();
//        model.addAttribute("chuongs", chuongs);
//        if (chuongOptional.isPresent()) {
//            Chuong chuong = chuongOptional.get();
//            model.addAttribute("chuong", chuong);
//            return "/chuongContent";
//        }
//        else
//            return "redirect:/error";
//    }


    //edit
    @GetMapping("/truyen/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        Optional<Truyen> detailTruyen = Optional.ofNullable(truyenService.getTruyenById(id));
        List<Chuong> chuong = chuongService.getChuongsByTruyenId(id);
        model.addAttribute("chuongs", chuong);
        if (detailTruyen.isPresent()) {
            Truyen truyen = detailTruyen.get();
            model.addAttribute("truyen", truyen);
            return "/truyenDetail";
        }
        else
            return "redirect:/error";
    }
    @GetMapping("truyen/{id_truyen}/chuong/{id_chuong}")
    public String Chuong_content(@PathVariable("id_truyen") Long id_truyen,
                                 @PathVariable("id_chuong") Long id_chuong, Model model){
        Truyen truyen = truyenService.getTruyenById(id_truyen);
        List<Chuong> chuong = chuongService.getChuongsByTruyenId(id_truyen);

        List<Anh> anh= anhService.getAnhsByChuongId(id_chuong);
        if (truyen == null) {
            if (!chuongService.isChuongBelongsToTruyen(id_truyen,id_chuong))
            { return "error/404"; } // Hoặc chuyển hướng đến trang lỗi 404}
        }
        // Đưa thông tin truyen và danh sách chuong vào model để hiển thị trên view
        model.addAttribute("anh",anh);
        model.addAttribute("truyen", truyen);
        model.addAttribute("chuong", chuong);
        model.addAttribute("chuongHienTai", chuong);

        return "/chuongContent";
    }

}
