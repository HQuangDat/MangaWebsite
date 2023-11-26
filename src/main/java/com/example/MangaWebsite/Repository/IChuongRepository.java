package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IChuongRepository extends JpaRepository<Chuong, Long> {
    List<Chuong> findAllById(Long truyenId);

    List<Chuong> findAllByUserId(Long userId);

    List<Chuong> findAllByTruyenId(Long truyenId);
}
