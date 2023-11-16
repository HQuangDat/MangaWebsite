package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "mangaStatus")
public class TrangThaiTruyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;


    @Column(name = "TenTrangThai")
    private String tentrangthai;
    @OneToMany(mappedBy = "trangThaiTruyenid")
    private Set<Truyen> truyens;

}
