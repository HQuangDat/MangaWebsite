package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Anh;
import com.example.MangaWebsite.Model.Category;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Truyen;


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
import com.example.MangaWebsite.Service.CategoryService;
import com.example.MangaWebsite.Service.ChuongService;
import com.example.MangaWebsite.Service.TruyenService;
import com.example.MangaWebsite.Service.UserService;
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
    public ResponseEntity<String> addTruyen(@PathVariable("userId") Long userId,
                                            @ModelAttribute("truyen") @Valid Truyen truyen,
                                            @RequestParam("avatar") MultipartFile avatarFile,
                                            BindingResult result,
                                            Model model,
                                            Authentication authentication
    ) {

        if (result.hasErrors()) {
            // Xử lý lỗi kiểm tra hợp lệ
            return new ResponseEntity<>("Có lỗi kiểm tra hợp lệ trong biểu mẫu", HttpStatus.BAD_REQUEST);
        }

        try {
            // Kiểm tra rằng tệp avatar không trống
            if (avatarFile.isEmpty()) {
                return new ResponseEntity<>("Yêu cầu tệp avatar", HttpStatus.BAD_REQUEST);
            }

            long maxSize = 10 * 1024 * 1024; // 10MB
            if (avatarFile.getSize() > maxSize) {
                return new ResponseEntity<>("Kích thước tệp quá lớn, vui lòng chọn tệp nhỏ hơn " + maxSize + " bytes", HttpStatus.BAD_REQUEST);
            }

// Kiểm tra loại nội dung
            String allowedContentType = "image/*";
            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                return new ResponseEntity<>("Loại tệp không được hỗ trợ, vui lòng chọn tệp hình ảnh", HttpStatus.BAD_REQUEST);
            }

// Lưu tên tệp và đường dẫn thực tế
            String fileName = avatarFile.getOriginalFilename();
            Path filePath = Paths.get("imgtaive", fileName);

            try {
                // Đảm bảo thư mục "imgtaive" đã tồn tại
                if (!Files.exists(filePath.getParent())) {
                    Files.createDirectories(filePath.getParent());
                }

                // Ghi tệp vào đường dẫn
                Files.write(filePath, avatarFile.getBytes());
            } catch (IOException e) {
                return new ResponseEntity<>("Lỗi khi lưu tệp: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
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
            truyen.setAvatarFilePath(filePath.toString());

            // Nhận danh mục được chọn dựa trên ID từ biểu mẫu
            if (truyen.getCategory() == null || truyen.getCategory().getId() == null) {
                return new ResponseEntity<>("Danh mục không hợp lệ", HttpStatus.BAD_REQUEST);
            }
            Category selectedCategory = categoryService.getCategoryById(truyen.getCategory().getId());
            if (selectedCategory == null) {
                return new ResponseEntity<>("Danh mục không tồn tại", HttpStatus.BAD_REQUEST);
            }
            // Đặt danh mục trong đối tượng truyen
            truyen.setCategory(selectedCategory);
            truyen.setNgayDang(LocalDateTime.now());
            // Lưu dữ liệu vào cơ sở dữ liệu bằng MangaService
            truyenService.addTruyen(truyen);

            return new ResponseEntity<>("Truyện đã được thêm thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi thêm Truyện: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/CTV-add-chuong")
    public ResponseEntity<String> createChuong(
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
                return new ResponseEntity<>("Người dùng không có quyền thêm chương cho truyện này", HttpStatus.FORBIDDEN);
            }

            // Lọc danh sách truyện chỉ hiển thị những truyện thuộc về người dùng
            List<Truyen> truyens = truyenService.getTruyensByUserId(userId);

            // Kiểm tra xem chương được thêm có thuộc về truyện của người dùng hay không
            if (chuong.getTruyen() == null || !truyens.contains(chuong.getTruyen())) {
                return new ResponseEntity<>("Chương không thuộc về truyện của người dùng", HttpStatus.BAD_REQUEST);
            }
            // Nhận danh mục được chọn dựa trên ID từ biểu mẫu
            if (chuong.getTruyen() == null || chuong.getTruyen().getId() == null) {
                return new ResponseEntity<>("Danh mục không hợp lệ", HttpStatus.BAD_REQUEST);
            }

            Truyen selectedTruyen = truyenService.getTruyenById(chuong.getTruyen().getId());
            if (selectedTruyen == null) {
                return new ResponseEntity<>("Danh mục không tồn tại", HttpStatus.BAD_REQUEST);
            }
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
                return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.BAD_REQUEST);
            }

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

            return new ResponseEntity<>("Thêm chương và ảnh thành công", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Lỗi khi thêm chương và ảnh: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint để lấy tất cả các Chương của một Truyện
    @GetMapping("/{userId}/CTV-add-chuong")
    public String showAddChuongForm( @PathVariable("userId") Long userId, Model model, Authentication authentication) {


        Chuong chuong = new Chuong();
        model.addAttribute("authentication", authentication);
        model.addAttribute("chuong", chuong);
        model.addAttribute("truyen", truyenService.getAllTruyens());
    // Lọc danh sách truyện chỉ hiển thị những truyện thuộc về người dùng
        List<Truyen> userTruyens = truyenService.getTruyensByUserId(userId);
        model.addAttribute("truyen", userTruyens);
        return "CTV/CTV-add-chuong";
    }
    // Endpoint để tải lên một ảnh cho một Chương
    private String saveImageToDatabase(MultipartFile image, String tenTruyen, String tenChuong) throws IOException {
        // Kiểm tra kích thước tệp
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (image.getSize() > maxSize) {
            throw new IOException("Kích thước tệp quá lớn, vui lòng chọn tệp nhỏ hơn " + maxSize + " bytes");
        }

        // Kiểm tra loại nội dung
        String allowedContentType = "image/*";
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new IOException("Loại tệp không được hỗ trợ, vui lòng chọn tệp hình ảnh");
        }

        // Xây dựng đường dẫn thư mục
        Path directoryPath = Paths.get("imgtaive", tenTruyen, tenChuong);

        // Lưu ảnh vào thư mục
        String fileName = image.getOriginalFilename();
        Path filePath = directoryPath.resolve(fileName);

        try {
            // Đảm bảo thư mục đã tồn tại hoặc tạo mới nếu chưa tồn tại
            Files.createDirectories(directoryPath);

            // Ghi tệp vào đường dẫn
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            throw new IOException("Lỗi khi lưu ảnh: " + e.getMessage());
        }

        // Trả về URL của ảnh (đường dẫn tương đối)
        return "/" + directoryPath.toString() + "/" + fileName;
    }

}
