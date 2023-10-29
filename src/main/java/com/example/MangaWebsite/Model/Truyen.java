package com.example.MangaWebsite.Model;

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
    @JoinColumn(name = "caterory_id")
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

    @OneToMany(mappedBy = "truyen")
    private Set<User> users;
}
