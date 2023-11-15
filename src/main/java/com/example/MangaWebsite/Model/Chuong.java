package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.Role;
import com.example.MangaWebsite.Entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="chuong")
public class Chuong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_truyen" ,referencedColumnName = "id", nullable = false)
    private Truyen truyen;

    @Column(name = "ten_chuong")
    private String tenChuong;

    @Lob
    @Column(name = "noi_dung")
    private byte[] noiDung;

    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Chuong() {
    }

    public Chuong(String tenChuong, LocalDateTime ngayDang, User user,Truyen truyen) {
        this.tenChuong = tenChuong;
        this.ngayDang = ngayDang;
        this.user = user;
        this.truyen = truyen;
    }

  /*  @OneToMany(mappedBy = "chuong", fetch = FetchType.LAZY)
    private Set<User> users;*/



}
