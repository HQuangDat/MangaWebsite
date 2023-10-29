package com.example.MangaWebsite.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TenLoaiTK",length = 50,nullable = false)
    @Size(max = 50, message = "Ten loai is required")
    private String TenLoaiTK;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
