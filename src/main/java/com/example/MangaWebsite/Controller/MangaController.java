package com.example.MangaWebsite.Controller;

import com.example.MangaWebsite.Entity.CustomUserDetail;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Category;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Model.Anh;
import com.example.MangaWebsite.Repository.IChuongRepository;
import com.example.MangaWebsite.Service.*;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("admin/truyen/")
public class MangaController {
    @Autowired
    private TruyenService truyenService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChuongService chuongService;
    @Autowired
    private AnhService anhService;

    @GetMapping("List/{id}")
    public ResponseEntity<?> getTruyen(@PathVariable String id) {
        try {
            long truyenId = Long.parseLong(id);
            Truyen truyen = truyenService.getTruyenById(truyenId);
            if (truyen != null) {
                return new ResponseEntity<>(truyen, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Truyen not found", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            // Xử lý ngoại lệ nếu id không thể chuyển đổi thành kiểu long
            return new ResponseEntity<>("Lỗi này này", HttpStatus.BAD_REQUEST);
        }
    }

// Hiển thị trang thêm truyện (GET)
    @GetMapping("/add")
    public String   AddTruyenForm(Model model) {
        // Tạo một đối tượng Truyen để binding với form
        Truyen truyen = new Truyen();

        // Truyền đối tượng Truyen và danh sách các danh mục vào model
        model.addAttribute("truyen", truyen);
        model.addAttribute("categories", categoryService.getAllCategories());

        // Trả về tên view (thường là tên của trang thêm truyện)
        return "truyen/add";
    }
    @PostMapping("/add")
    public ResponseEntity<String> addTruyen(@ModelAttribute("truyen") @Valid Truyen truyen,
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




    // Xóa truyện
    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteTruyen(@PathVariable long id) {
        truyenService.deleteTruyen(id);
        return new ResponseEntity<>("Truyện đã được xóa thành công", HttpStatus.OK);
    }
    @PostMapping("/delete")
    public ResponseEntity<String> deleteTruyen(@RequestParam("truyenId") Long truyenId,
                                               Authentication authentication) {
        try {
            // Nhận thông tin người dùng hiện tại từ Authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();

            // Lấy ID của người dùng từ service
            Long currentUserId = userService.getUserIdByUsername(currentUsername);

            // Kiểm tra xem truyện có thuộc về người dùng hiện tại không
            Truyen truyen = truyenService.getTruyenById(truyenId);

            if (truyen != null && truyen.getUser() != null && truyen.getUser().getId().equals(currentUserId)) {
                // Xóa truyện nếu nó thuộc về người dùng hiện tại
                truyenService.deleteTruyen(truyenId);
                return new ResponseEntity<>("Truyện đã được xóa thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không tìm thấy hoặc bạn không có quyền xóa truyện này", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi xóa Truyện: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Chỉnh sửa truyện
    // Lấy thông tin của một manga dựa vào ID
    @GetMapping("/edit/{id}")
    public String editTruyenForm(@PathVariable("id") Long id, Model model) {
        Optional<Truyen> editTruyen = Optional.ofNullable(truyenService.getTruyenById(id));
        if (editTruyen.isPresent()) {
            Truyen truyen = editTruyen.get();
            model.addAttribute("truyen", truyen);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("selectedCategoryId", truyen.getCategory().getId());
            return "truyen/edit";
        } else {
            return "not-found";
        }
    }

    @PostMapping("/edit")
    public String editTruyen(@ModelAttribute("truyen") Truyen truyen) {
        truyenService.updateTruyen(truyen);
        return "redirect:/list"; // Sửa đường dẫn chuyển hướng
    }


    @GetMapping("/list")
    public String listTruyens(Model model) {
        List<Truyen> truyens = truyenService.getAllTruyens();
        model.addAttribute("truyens", truyens);
        return "truyen/list";
    }
    @GetMapping("/list/{userId}")
    public String listTruyensByUserId(@PathVariable Long userId, Model model) {
        List<Truyen> truyens = truyenService.getTruyensByUserId(userId);
        model.addAttribute("truyens", truyens);
        return "truyen/list";
    }

    //<----------------------------------------------------------------------------------------->
    //Chương và Ảnh
    // Endpoint để tạo một Chương mới cho Truyện
    @PostMapping("/add-chuong")
    public ResponseEntity<String> createChuong(
            @ModelAttribute("chuong") @Valid Chuong chuong,
            @RequestParam("anhChuong") MultipartFile[] anhFiles,
            BindingResult result,
            Model model,
            Authentication authentication
    ) {
        try {
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
                String imageUrl = saveImageToDatabase(image, chuong.getTruyen().getTenTruyen(), chuong.getId());
                Anh anh = new Anh();
                anh.setChuong(chuong);
                anh.setDuongDan(imageUrl);
                anhList.add(anh);
            }

            // Liên kết ảnh với chương
            chuong.setAnhList(anhList);



            // Liên kết chương với người dùng hiện tại
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
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

            return new ResponseEntity<>("Thêm chương và ảnh thành công", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Lỗi khi thêm chương và ảnh: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint để lấy tất cả các Chương của một Truyện
            @GetMapping("/add-chuong")
            public String showAddChuongForm( Model model) {


                Chuong chuong = new Chuong();

                model.addAttribute("chuong", chuong);
                model.addAttribute("truyen", truyenService.getAllTruyens());

                return "truyen/add-chuong";
            }
    // Endpoint để tải lên một ảnh cho một Chương
    private String saveImageToDatabase(MultipartFile image, String tenTruyen, Long chuongId) throws IOException {
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
        Path directoryPath = Paths.get("imgtaive", tenTruyen, chuongId.toString());

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


    // Endpoint để lấy tất cả các ảnh của một Chương
    @GetMapping("/{truyenId}/chuongs/{chuongId}/anhs")
    public List<Anh> getAnhsByChuongId(@PathVariable Long truyenId, @PathVariable Long chuongId) {
        // Lấy danh sách các ảnh cho chương có id chuongId của truyện có id truyenId
        // ...

        return anhService.getAnhsByChuongId(chuongId);
    }

    // ...
}



