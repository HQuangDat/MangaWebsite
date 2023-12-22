package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_cmt")

public class Comment {

    // Getters and Setters
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "truyen_id")
    private Truyen truyen;

    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @Column(name = "mieu_ta", columnDefinition = "TEXT")
    @Lob
    private String NoiDung;

    @Getter
    @Setter
    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;
    // Constructors, getters, and setters

    // Constructors


    public void setId(Long id) {
        this.id = id;
    }

    public void setTruyen(Truyen truyen) {
        this.truyen = truyen;
    }

    public void setUser(User user) {
        this.user = user;
    }


}