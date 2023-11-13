package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Validator.annotation.ValidCategoryId;
import com.example.MangaWebsite.Validator.annotation.ValidUserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @ValidCategoryId
    private Category category;

    @ManyToOne
    @JoinColumn(name = "chuong_id")
    private Chuong chuong;

    @Column(name = "so_chuong")
    private int SoChuong;

    @ManyToOne
    @JoinColumn(name = "manga_status_id")
    private TrangThaiTruyen trangThaiTruyen;

    @Column(name = "so_like")
    private int SoLike;

    @Column(name = "so_cmt")
    private int SoCmt;

    @ManyToOne
    @JoinColumn(name = "premium_id")
    private Premium premium;

    @Column(name = "ten_truyen")
    @NotNull(message = "Ten truyen is required")
    private String TenTruyen;

    @Column(name = "ten_tac_gia")
    @NotNull(message = "Ten tac gia is required")
    private String TenTacGia;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ValidUserId
    private  User user;

    @OneToMany(mappedBy = "truyen")
    private Set<User> users;
    @OneToMany(mappedBy = "truyen")
    private Set<Chuong> chuongs;
}
