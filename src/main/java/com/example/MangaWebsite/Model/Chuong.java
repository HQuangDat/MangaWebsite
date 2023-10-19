package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name="chuong")
public class Chuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private char MaChuong;
    @Column(name = "TenChuong")
    private String TenChuong;
    @Column(name = "MaTruyen")
    private char MaTruyen;
    @Lob
    @Column(name = "NoiDung", columnDefinition = "longblob")
    private byte[] NoiDung;

    @OneToMany(mappedBy = "chuong")
    private Set<Truyen> truyens;
}
