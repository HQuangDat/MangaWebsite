package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.TrangThaiTruyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITrangThaiTruyenRepository extends JpaRepository<TrangThaiTruyen, Long> {

    List<TrangThaiTruyen> findAll();
}
