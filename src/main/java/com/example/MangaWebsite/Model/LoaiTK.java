package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "loaitk")
public class LoaiTK {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private char MaLoaiTK;

    @Column(name = "TenLoaiTK")
    private String TenLoaiTK;

    @OneToMany(mappedBy = "loaitk")
    private Set<TaiKhoan> taikhoans;
}
