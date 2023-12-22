package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
@Entity
@Table(name = "mangaStatus")
public class TrangThaiTruyen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;


    @Column(name = "TenTrangThai")
    @Getter
    @Setter
    private String tentrangthai;

    @OneToMany(mappedBy = "trangThaiTruyen")
    private Set<Truyen> truyens;




}
