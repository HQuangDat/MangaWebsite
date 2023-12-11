package com.example.MangaWebsite.Model;

import com.example.MangaWebsite.Validator.annotation.ValidCategoryId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Data
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_the_loai")
    private String TenTheLoai;

}
