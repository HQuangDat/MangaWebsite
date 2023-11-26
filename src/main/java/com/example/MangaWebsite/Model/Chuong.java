package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.Role;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Validator.annotation.ValidUserId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
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


    @ManyToOne
    @JoinColumn(name = "ma_truyen" ,referencedColumnName = "id", nullable = false)
    private Truyen truyen;

    @Column(name = "ten_chuong")
    private String tenChuong;

    @Column(name = "ngay_dang")
    private LocalDateTime ngayDang;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ValidUserId
    private  User user;

    @OneToMany(mappedBy = "chuong", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Anh> anhList;


    public Chuong(String tenChuong, LocalDateTime ngayDang, User user,Truyen truyen) {
        this.tenChuong = tenChuong;
        this.ngayDang = ngayDang;
        this.user = user;
        this.truyen = truyen;
    }

    public Chuong() {

    }

  /*  @OneToMany(mappedBy = "chuong", fetch = FetchType.LAZY)
    private Set<User> users;*/



}
