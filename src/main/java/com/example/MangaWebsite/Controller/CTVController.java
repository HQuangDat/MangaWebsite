package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.*;


import com.example.MangaWebsite.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@Controller
@RequestMapping("/CTV")
public class CTVController {
    @Autowired
    private TruyenService truyenService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;
    @Autowired
    private ChuongUserService chuongUserService;

    @Autowired
    private ChuongService chuongService;
    @GetMapping("/{userId}/CTV-list")
    public String CTVGetTruyen(@PathVariable Long userId, Model model,Authentication authentication) {
        List<Truyen> truyens = truyenService.getTruyensByUserId(userId);
        model.addAttribute("authentication", authentication);
        model.addAttribute("truyens", truyens);
        return "CTV/CTV-list";
    }
    @GetMapping("/{userId}/{truyenId}/CTV-list-chuong")
    public String getTruyenAndChuongsByUserId(
            @PathVariable("userId") Long userId,
            @PathVariable("truyenId") Long truyenId,
            Model model) {

        // Kiểm tra xem truyen có tồn tại hay không, nếu không thì trả về 404

        Truyen truyen = truyenService.getTruyenById(truyenId);
        if (truyen == null) {
            return "error/404";  // Hoặc chuyển hướng đến trang lỗi 404
        }

        // Lấy danh sách chương của truyen
        List<Chuong> chuong = chuongService.getChuongsByTruyenId(truyenId);

        // Đưa thông tin truyen và danh sách chuong vào model để hiển thị trên view
        model.addAttribute("truyen", truyen);
        model.addAttribute("chuong", chuong);

        // Trả về tên của view template hiển thị thông tin truyen và danh sách chuong
        return "CTV/CTV-list-chuong";
    }
    // Hiển thị trang thêm truyện (GET)
    @GetMapping("/{userId}/CTV-add")
    public String AddTruyenFormByUser( @PathVariable("userId") Long userId,Model model, Authentication authentication) {
        // Tạo một đối tượng Truyen để binding với form
        Truyen truyen = new Truyen();
        model.addAttribute("authentication", authentication);
        // Truyền đối tượng Truyen và danh sách các danh mục vào model
        model.addAttribute("truyen", truyen);
        model.addAttribute("categories", categoryService.getAllCategories());

        // Trả về tên view (thường là tên của trang thêm truyện)
        return "CTV/CTV-add";
    }
    @PostMapping("/{userId}/CTV-add")
    public String addTruyen(@PathVariable("userId") Long userId,
                                            @ModelAttribute("truyen") @Valid Truyen truyen,
                                            @RequestParam("avatar") MultipartFile avatarFile,
                                            BindingResult result,
                                            Model model,
                                            Authentication authentication
    ) {

        if (result.hasErrors()) {
            // Xử lý lỗi kiểm tra hợp lệ
            return "redirect:/CTV/{userId}/CTV-add?error";    }

        try {
            // Kiểm tra rằng tệp avatar không trống
            if (avatarFile.isEmpty()) {
                return "redirect:/CTV/{userId}/CTV-add?error";
            }

            long maxSize = 10 * 1024 * 1024; // 10MB
            if (avatarFile.getSize() > maxSize) {
                return "redirect:/CTV/{userId}/CTV-add?error";
            }

// Kiểm tra loại nội dung
            String allowedContentType = "image/*";
            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                return "redirect:/CTV/{userId}/CTV-add?error";
            }
            // Xây dựng đường dẫn thư mục
            String fileName = avatarFile.getOriginalFilename();
            String sanitizedFileName = sanitizeFileName(fileName);

            Path directoryPath = Paths.get("E:/GCWT2", sanitizeFileName(truyen.getTenTruyen()));

            // Lưu ảnh vào thư mục
            Path filePath = directoryPath.resolve(sanitizedFileName);

            // Đảm bảo thư mục đã tồn tại hoặc tạo mới nếu chưa tồn tại
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Ghi tệp vào đường dẫn
            Files.write(filePath, avatarFile.getBytes());
            // Nhận thông tin người dùng hiện tại từ SecurityContext
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();

            // Bạn có thể sử dụng username để lấy ID của người dùng từ service
            Long currentUserId = userService.getUserIdByUsername(currentUsername);

            // Đặt giá trị cho trường user trong đối tượng Truyen
            User currentUser = userService.getUserbyId(currentUserId);
            truyen.setUser(currentUser);

            // Lưu thông tin vào đối tượng truyen
            truyen.setAvatarFileName(fileName);
            // Nhận danh mục được chọn dựa trên ID từ biểu mẫu
            if (truyen.getCategory() == null || truyen.getCategory().getId() == null) {
                return "redirect:/CTV/{userId}/CTV-add?error";  }
            Category selectedCategory = categoryService.getCategoryById(truyen.getCategory().getId());
            if (selectedCategory == null) {
                return "redirect:/CTV/{userId}/CTV-add?error";  }
            // Đặt danh mục trong đối tượng truyen
            truyen.setCategory(selectedCategory);
            truyen.setNgayDang(LocalDateTime.now());
            // Lưu dữ liệu vào cơ sở dữ liệu bằng MangaService
            truyenService.addTruyen(truyen);

            return "redirect:/CTV/{userId}/CTV-add?success";
        } catch (Exception e) {
            return "redirect:/CTV/{userId}/CTV-add?error";
        }
    }

    private String sanitizeFileName(String fileName) {
        // Chuyển đổi tên file thành không dấu và thay thế khoảng trắng bằng dấu _
        String fileNameWithoutAccent = convertToAscii(fileName);
        return fileNameWithoutAccent.replaceAll("\\s", "_");
    }

    private String convertToAscii(String input) {
        return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }
    @PostMapping("/{userId}/CTV-add-chuong")
    public String createChuong(
            @PathVariable("userId") Long userId,
            @ModelAttribute("chuong") @Valid Chuong chuong,
            @RequestParam("anhChuong") MultipartFile[] anhFiles,
            BindingResult result,
            Model model,
            Authentication authentication
    ) {
        try {
            // Lấy thông tin người dùng đăng nhập
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Kiểm tra xem người dùng đăng nhập có đúng là chủ sở hữu của truyện hay không
            if (!userId.equals(userService.getUserIdByUsername(userDetails.getUsername()))) {
                return "redirect:/CTV/{userId}/CTV-add-chuong?error";
            }

            // Lọc danh sách truyện chỉ hiển thị những truyện thuộc về người dùng
            List<Truyen> truyens = truyenService.getTruyensByUserId(userId);

            // Nhận danh mục được chọn dựa trên ID từ biểu mẫu
            if (chuong.getTruyen() == null || chuong.getTruyen().getId() == null) {
                return "redirect:/CTV/{userId}/CTV-add-chuong?error"; }

            Truyen selectedTruyen = truyenService.getTruyenById(chuong.getTruyen().getId());
            if (selectedTruyen == null) {
                return "redirect:/CTV/{userId}/CTV-add-chuong?error"; }
            chuong.setTruyen(selectedTruyen);

            List<Anh> anhList = new ArrayList<>();

            // Nếu chuong chưa có id, thêm chuong vào cơ sở dữ liệu để có id
            if (chuong.getId() == null) {
                // Lưu chương vào cơ sở dữ liệu
                chuongService.addChuongs(chuong);
            }

            // Lưu các URL của ảnh vào table Anh
            for (MultipartFile image : anhFiles) {
                String imageUrl = saveImageToDatabase(image, chuong.getTruyen().getTenTruyen(), chuong.getTenChuong());
                Anh anh = new Anh();
                anh.setChuong(chuong);
                anh.setDuongDan(imageUrl);
                anhList.add(anh);
            }

            // Liên kết ảnh với chương
            chuong.setAnhList(anhList);



            // Liên kết chương với người dùng hiện tại
            String currentUsername = userDetails.getUsername();

            // Bạn có thể sử dụng username để lấy ID của người dùng từ service
            Long currentUserId = userService.getUserIdByUsername(currentUsername);

            // Đặt giá trị cho trường user trong đối tượng Chuong
            User currentUser = userService.getUserbyId(currentUserId);
            if (currentUser == null) {
                return "redirect:/CTV/{userId}/CTV-add-chuong?error";}

            // Đặt giá trị cho trường user trong đối tượng Chuong
            chuong.setUser(currentUser);
            chuong.setNgayDang(LocalDateTime.now());
            for (Anh anh : chuong.getAnhList()) {
                anh.setChuong(chuong);
            }
            // Thêm chương
            chuongService.addChuongs(chuong);
            // Lấy thông tin truyện từ chương vừa thêm
            Truyen truyen = chuong.getTruyen();

            // Lấy số lượng chương hiện tại của truyện
            int soLuongChuongHienTai = truyen.getSoChuong();

            // Cập nhật số lượng chương
            truyen.setSoChuong(soLuongChuongHienTai + 1);

            // Lưu truyện đã cập nhật vào cơ sở dữ liệu
            truyenService.updateTruyen(truyen);

            return "redirect:/CTV/{userId}/CTV-add-chuong?success";
        } catch (IOException e) {
            return "redirect:/CTV/{userId}/CTV-add-chuong?error";  }
    }


    // Endpoint để lấy tất cả các Chương của một Truyện
    @GetMapping("/{userId}/CTV-add-chuong")
    public String showAddChuongForm( @PathVariable("userId") Long userId, Model model, Authentication authentication) {


        Chuong chuong = new Chuong();
        model.addAttribute("authentication", authentication);
        model.addAttribute("chuong", chuong);
        model.addAttribute("truyen", truyenService.getAllTruyens());
        List<Truyen> userTruyens = truyenService.getTruyensByUserId(userId);
        model.addAttribute("truyen", userTruyens);
        return "CTV/CTV-add-chuong";
    }

    private String saveImageToDatabase(MultipartFile image, String tenTruyen, String tenChuong) throws IOException {

        long maxSize = 10 * 1024 * 1024; // 10MB
        if (image.getSize() > maxSize) {
            throw new IOException("Kích thước tệp quá lớn, vui lòng chọn tệp nhỏ hơn " + maxSize + " bytes");
        }

        String allowedContentType = "image/*";
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new IOException("Loại tệp không được hỗ trợ, vui lòng chọn tệp hình ảnh");
        }
        String fileName = image.getOriginalFilename();
        String sanitizedFileName = sanitizeFileName(fileName);
        Path directoryPath = Paths.get("E:/GCWT2", sanitizeFileName(tenTruyen),sanitizeFileName(tenChuong));
        Path filePath = directoryPath.resolve(sanitizedFileName);
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        try {
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            throw new IOException("Lỗi khi lưu ảnh: " + e.getMessage());
        }
        return sanitizeFileName(tenTruyen)+"/"+sanitizeFileName(tenChuong)+"/"+sanitizedFileName;
    }

    //-------------EDIT TRUYEN------------------------------------------------
    @GetMapping("/{userId}/{truyenId}/CTV-edit-truyen")
    public String showCTVEdittruyen(
            @PathVariable("userId") Long userId,
            @PathVariable Long truyenId,
            Model model,
            Authentication authentication) {
        Truyen truyen = truyenService.getTruyenById(truyenId);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("authentication", authentication);
        model.addAttribute("truyen",truyen);

        return "CTV/CTV-edit-truyen";
    }
    @PostMapping("/{userId}/{truyenId}/CTV-edit-truyen")
    public String editTruyen(
            @PathVariable Long userId,
            @PathVariable Long truyenId,
            @ModelAttribute("truyen") Truyen truyen,
            @RequestParam("avatar") MultipartFile avatarFile,
            BindingResult result,
            Model model,
            Authentication authentication
    ) {

        if (result.hasErrors()) {
            return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error"; }

        try {
            if (avatarFile.isEmpty()) {
                return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error";   }

            long maxSize = 10 * 1024 * 1024; // 10MB
            if (avatarFile.getSize() > maxSize) {
                return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error";  }

            String allowedContentType = "image/*";
            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error";}

            String fileName = avatarFile.getOriginalFilename();
            String sanitizedFileName = sanitizeFileName(fileName);

            Path directoryPath = Paths.get("E:/GCWT2", sanitizeFileName(truyen.getTenTruyen()));
            Path filePath = directoryPath.resolve(sanitizedFileName);

            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Files.write(filePath, avatarFile.getBytes());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            Long currentUserId = userService.getUserIdByUsername(currentUsername);

            User currentUser = userService.getUserbyId(currentUserId);
            truyen.setUser(currentUser);
            truyen.setAvatarFileName(fileName);

            if (truyen.getCategory() == null || truyen.getCategory().getId() == null) {
                return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error";  }

            Category selectedCategory = categoryService.getCategoryById(truyen.getCategory().getId());
            if (selectedCategory == null) {
                return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error";   }
            Truyen truyenEdit = truyenService.getTruyenById(truyenId);
            if (truyenEdit == null) {
                return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error";  }


            if (!Objects.equals(truyenEdit.getUser().getId(), currentUserId)) {
                return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error";  }


            truyenEdit.setTenTruyen(truyen.getTenTruyen());
            truyenEdit.setTacGia(truyen.getTacGia());
            truyenEdit.setMoTaNoiDung(truyen.getMoTaNoiDung());
            truyenEdit.setAvatarFileName(fileName);
            truyenEdit.setCategory(truyen.getCategory());

            truyenService.updateTruyen(truyenEdit);

            return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?success"; } catch (Exception e) {
            return "redirect:/CTV/{userId}/{truyenId}/CTV-edit-truyen?error";  }
    }
    //-------------DELETE TRUYEN------------------------------------------------
    @PostMapping("/{userId}/delete/{truyenId}")
    public String deleteTruyen(@PathVariable Long userId,
                               @PathVariable Long truyenId,
                               Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userId.equals(userService.getUserIdByUsername(userDetails.getUsername()))) {
            return "redirect:../CTV-list";
        }
        truyenService.deleteTruyen(truyenId);
        return "redirect:../CTV-list";
    }
    //-------------DELETE CHUONG------------------------------------------------
    @PostMapping("/{userId}/{truyenId}/{chuongId}/delete")
    public String deleteTruyen(@PathVariable Long userId,
                               @PathVariable Long truyenId,
                               @PathVariable Long chuongId,
                               Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Truyen truyen = truyenService.getTruyenById(truyenId);
        chuongUserService.DeleteAllByChuongId(chuongService.getChuongById(chuongId));
        truyen.setSoChuong(truyen.getSoChuong() - 1);
        truyenService.updateTruyen(truyen);
        if (!userId.equals(userService.getUserIdByUsername(userDetails.getUsername()))) {
            return "redirect:../CTV-list";
        }
       chuongService.deleteChuong(chuongId);

        return "redirect:../CTV-list-chuong";  // Chuyển hướng về trang danh sách sau khi xóa
    }
    //-------------Lich SU GIao DIch------------------------------------------------
    @GetMapping("/{userId}/CTV-lich-su")
    public String listMuaChuong(Model model,
                                @PathVariable Long userId) {
        User account = userService.getUserbyId(userId);
     List<Chuong_User> listdamua = chuongUserService.getChaptersAndUsersForAccount(account);
        model.addAttribute("listdamuas", listdamua);
        return "CTV/CTV-lich-su";
    }
}
