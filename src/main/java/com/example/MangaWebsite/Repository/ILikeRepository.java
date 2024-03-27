package com.example.MangaWebsite.Repository;


import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Like;
import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ILikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByTruyenAndUser(Truyen truyen, User user);
}