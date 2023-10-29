package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name="chuong")
public class Chuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TenChuong")

    private String TenChuong;

    @Column(name = "MaTruyen")
    private Long MaTruyen;

    @Lob
    @Column(name = "NoiDung", columnDefinition = "longblob")
    private byte[] NoiDung;

    @Column(name = "NgayDang")
    private LocalDateTime NgayDang;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "chuong")
    private Set<Truyen> truyens;
}
