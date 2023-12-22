package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IChuongRepository extends JpaRepository<Chuong, Long> {
    List<Chuong> findAllById(Long truyenId);

    List<Chuong> findAllByUserId(Long userId);

    List<Chuong> findAllByTruyenId(Long truyenId);

    Optional<Chuong> getChuongById(Long chuongId);

    Chuong findByTenChuong(String tenChuong);
    @Query("SELECT c FROM Chuong c WHERE c.ngayDang >= :startOfWeek")
    List<Chuong> findChuongByNgayDangInWeek(@Param("startOfWeek") LocalDateTime startOfWeek);

    List<Chuong> getChuongsByTruyenId(Long truyenId);

}
