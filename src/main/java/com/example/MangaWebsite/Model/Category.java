package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Validator.annotation.ValidCategoryId;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "category")
public class Category {
    @lombok.Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @lombok.Getter
    @Column(name = "ten_the_loai")
    private String TenTheLoai;

}
