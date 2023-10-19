package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "trangthaitruyen")
public class TrangThaiTruyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private char MaTrangThai;

    @Column(name = "TenTrangThai")
    private String TenTrangThai;

    @OneToMany(mappedBy = "trangthaitruyen")
    private Set<Truyen> truyens;
}
