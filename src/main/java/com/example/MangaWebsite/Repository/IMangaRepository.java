package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMangaRepository extends JpaRepository<Truyen, Long> {
    List<Truyen> findAllById(Long userId);
}
