package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Validator.annotation.ValidUserId;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "truyen")
public class Truyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "caterory_id" ,referencedColumnName = "id")
    private Category category;


    @Column(name = "SoChuong",nullable = true)
    private int SoChuong;

    // Đặt giá trị mặc định cho trangThaiTruyenid là 1 khi tạo mới đối tượng Truyen
// Giả sử 1 là giá trị mặc định bạn muốn set
        @ManyToOne
        @JoinColumn(name = "manga_status_id")
        private TrangThaiTruyen trangThaiTruyenid ;

    public Truyen() {
        // Tạo một đối tượng TrangThaiTruyen với id là 1 và set làm giá trị mặc định cho trangThaiTruyenid
        this.trangThaiTruyenid = new TrangThaiTruyen();
        this.trangThaiTruyenid.setId(1L);
    }


    @Column(name = "so_like",nullable = true)
    private int SoLike;

    @Column(name = "so_cmt",nullable = true)
    private int SoCmt;
    @Column(name = "mieu_ta")
    private String moTaNoiDung;


    @Column(name = "avatar_url")
    private String avatarFileName;

    @Transient
    private transient MultipartFile avatarFile;

    @Transient
    private String avatarFilePath;

    @ManyToOne
    @JoinColumn(name = "premium_id",nullable = true)
    private Premium premium;

    @Column(name = "ten_truyen",nullable = true)
    private String TenTruyen;

    @Column(name = "tac_gia",nullable = true)
    private  String TacGia;

    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ValidUserId
    private  User user;


    @OneToMany(mappedBy = "truyen")
    private Set<User> users;


    public void setUserId(Long currentUser) {

    } public String getName() {
        return TenTruyen;
    }
}