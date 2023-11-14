package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Validator.annotation.ValidUserId;
import jakarta.persistence.*;
import lombok.Data;

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
    @ValidUserId
    private Category category;

    @ManyToOne
    @JoinColumn(name = "chuong_id")
    private Chuong chuong;

    @Column(name = "SoChuong",nullable = true)
    private int SoChuong;

    @ManyToOne
    @JoinColumn(name = "manga_status_id")
    private TrangThaiTruyen trangThaiTruyen;

    @Column(name = "so_like",nullable = true)
    private int SoLike;

    @Column(name = "so_cmt",nullable = true)
    private int SoCmt;

    @ManyToOne
    @JoinColumn(name = "premium_id",nullable = true)
    private Premium premium;

    @Column(name = "ten_truyen",nullable = true)
    private String TenTruyen;

    @Column(name = "tac_gia",nullable = true)
    private  String TacGia;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ValidUserId
    private  User user;

    @OneToMany(mappedBy = "truyen")
    private Set<User> users;
}
