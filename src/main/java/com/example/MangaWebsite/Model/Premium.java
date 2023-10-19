package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "premium")
public class Premium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private char MaPhanLoai;
    @Column(name = "GiaTien",precision = 18, scale = 2)
    private BigDecimal GiaTien;

    @OneToMany(mappedBy = "premium")
    private Set<Truyen> truyens;
}
