package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends JpaRepository<TaiKhoan, Character> {
}
