package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IMangaRepository extends JpaRepository<Truyen, Long> {
    List<Truyen> findAllByUser_Id(Long userId);


    List<Truyen> findByNgayDangGreaterThanEqual(LocalDateTime from);

    List<Truyen> findByTenTruyenContainingAndTrangThaiTruyen_IdAndCategory_Id(String tenTruyen, Long trangThaiTruyenid, Long category);

    List<Truyen> findAllByCategory_Id(Long category);

    List<Truyen> findAllByTrangThaiTruyen_IdAndCategory_Id(Long trangThaiTruyenid, Long category);

    List<Truyen> findAllByTenTruyenContainingAndCategory_Id(String tenTruyen, Long category);
    List<Truyen> findAllByTrangThaiTruyen_Id(Long trangThaiTruyenid);

    List<Truyen> findAllByTenTruyenContainingAndTrangThaiTruyen_Id(String tenTruyen, Long trangThaiTruyenid);

    List<Truyen> findAllByTenTruyenContaining(String tenTruyen);
}
