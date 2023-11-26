package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Anh;
import com.example.MangaWebsite.Model.Chuong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAnhRepository extends JpaRepository<Anh, Long> {
    // Các phương thức truy vấn cụ thể nếu cần
    List<Anh> findByChuongId(Long chuongId);
}
