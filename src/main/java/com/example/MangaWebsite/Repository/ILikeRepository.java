package com.example.MangaWebsite.Repository;


import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Like;
import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByTruyenAndUser(Truyen truyen, User user);
}