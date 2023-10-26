package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "taikhoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private char MaTK;

    @ManyToOne
    @JoinColumn(name = "MaLoaiTK")
    private LoaiTK MaLoaiTK;

    @Column(name = "Username")
    private String Username;
    @Column(name = "Password")
    private String Password;
    @Column(name = "TenUser")
    private String TenUser;
    @Column(name = "CCCD")
    private char CCCD;
    @Column(name = "NgaySinh")
    private Date NgaySinh;
    @Column(name = "SDT")
    private char SDT;
    @Column(name = "SoDu")
    private int SoDu;

    @Lob
    @Column(name = "Avatar", columnDefinition = "longblob")
    private byte[] Avatar;

    @ManyToOne
    @JoinColumn(name = "MaTruyen")
    private Truyen MaTruyen;

    @OneToMany
    private Set<Chuong> chuongs;
}
