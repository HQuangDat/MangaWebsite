package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "review")

public class Review {


    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "truyen_url")
    private String  truyen_url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "mieu_ta", columnDefinition = "TEXT")
    @Lob
    private String NoiDung;

    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;


    // Constructors, getters, and setters

    // Constructors
    public Review(String Truyen_url, User user,String NoiDung, LocalDateTime ngayDang) {
        this.NoiDung = NoiDung;
        this.ngayDang = ngayDang;
        this.truyen_url = Truyen_url;
        this.user = user;
    }

    public Review() {

    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setTruyen_url(String truyen_url) {
        this.truyen_url = truyen_url;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setNoiDung(String noidung){this.NoiDung
     = noidung;}
    public void setNgayDang(LocalDateTime ngaydang){this.ngayDang
            = ngaydang;}
}


