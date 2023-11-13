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

    @Column
    private int SoChuong;

    @ManyToOne
    @JoinColumn(name = "manga_status_id")
    private TrangThaiTruyen trangThaiTruyen;

    @Column(name = "SoLike")
    private int SoLike;

    @Column(name = "SoCmt")
    private int SoCmt;

    @ManyToOne
    @JoinColumn(name = "premium_id")
    private Premium premium;

    @Column(name = "TenTruyen")
    private String TenTruyen;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ValidUserId
    private  User user;

    @OneToMany(mappedBy = "truyen")
    private Set<User> users;
}
