package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "loaitruyen")
public class LoaiTruyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private char MaTheLoai;

    @Column(name = "TenTheLoai")
    private String TenTheLoai;

    @OneToMany(mappedBy = "loaitruyen")
    private Set<Truyen> truyens;
}
