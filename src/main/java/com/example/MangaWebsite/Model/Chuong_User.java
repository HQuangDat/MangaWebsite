package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "chuong_user")
@IdClass(ChuongUserId.class)
public class Chuong_User implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuong_id")
    private Chuong chuongId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "ngay_mua", nullable = false)
    private LocalDateTime ngay_mua;

    @Column(name = "gia", nullable = false)
    private int gia;

    // other fields and methods
}
