package com.example.MangaWebsite.Entity;

import com.example.MangaWebsite.Entity.User;
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

    @Column(name = "ten_loaitk",length = 50,nullable = false)
    @Size(max = 50, message = "Ten loai is required")
    private String TenLoaiTK;

    @ManyToMany
    @JoinTable(name = "user_role",
        joinColumns= @JoinColumn(name = "user_id"),
        inverseJoinColumns =@JoinColumn(name = "role_id"))
    private Set<Role> roles =new HashSet<>();


    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
