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
    private char MaTruyen;

    @ManyToOne
    @JoinColumn(name = "MaTheLoai")
    private LoaiTruyen MaTheLoai;

    @ManyToOne
    @JoinColumn(name = "MaChuong")
    private Chuong MaChuong;

    @Column
    private int SoChuong;

    @ManyToOne
    @JoinColumn(name = "MaTrangThai")
    private TrangThaiTruyen MaTrangThai;

    @Column(name = "SoLike")
    private int SoLike;
    @Column(name = "SoCmt")
    private int SoCmt;

    @ManyToOne
    @JoinColumn(name = "MaPhanLoai")
    private Premium MaPhanLoai;

    @Column(name = "TenTruyen")
    private String TenTruyen;

    //Forgein Key for taikhoan
    @OneToMany(mappedBy = "truyen")
    private Set<TaiKhoan> taikhoans;
}
