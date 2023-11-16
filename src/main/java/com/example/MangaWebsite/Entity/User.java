package com.example.MangaWebsite.Entity;

import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Chuong_User;
import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Validator.annotation.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 50, nullable = false)
    @NotBlank(message = "Username must required")
    @Size(max = 50,message = "Username must less than 50 characters")
    @ValidUsername
    private String username;

    @Column(name = "password", length = 500, nullable = false)
    @NotBlank(message = "Password must required")
    @Size(max = 500,message = "Password must less than 50 characters")
    private String password;

    @Column(name = "displayname", length = 200, nullable = false)
    @NotBlank(message = "Display name must be required")
    @Size(max = 200,message = "Display name must less than 200 characters")
    @Getter
    private String displayname;

    @Column(name = "CCCD", length = 13)
    @Size(max = 13,message = "CCCD must less than 13 characters")
    private String CCCD;

    @Column(name = "ngaysinh", nullable = false)
    private Date  ngaysinh;

    @Column(name = "SDT", length = 13, nullable = false)
    @NotBlank(message = "SDT must be required")
    @Size(max = 13,message = "SDT must less than 13 characters")
    private String SDT;

    @Column(name = "so_du",nullable = true)
    private int SoDu;

    @Lob
    @Column(name = "Avatar", columnDefinition = "longblob",nullable = true)
    private byte[] Avatar;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truyen_id" ,referencedColumnName = "id", nullable = true)
    private Truyen truyen;

    @OneToMany(mappedBy = "user")
    private Set<Chuong> chuongs;
    @OneToMany(mappedBy = "user")
    private Set<Truyen> truyens;
    @OneToMany(mappedBy = "user")
    private Set<Chuong_User> chuong_users;
}
