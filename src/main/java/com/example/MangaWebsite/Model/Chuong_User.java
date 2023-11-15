package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;


@Data
    @Entity
    @Table(name = "chuong_user")
    public class Chuong_User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "chuong_id")
        private Chuong chuongId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        @Column(name = "price", nullable = false)
        private double price;

        public Chuong_User() {
        }

        public Chuong_User(Chuong chuongId, User userId, double price,LocalDateTime createdAt) {
            this.chuongId = chuongId;
            this.user = userId;
            this.price = price;
            this.createdAt = createdAt;
        }
    }


