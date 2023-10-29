package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMangaRepository extends JpaRepository<Truyen, Long> {
}
