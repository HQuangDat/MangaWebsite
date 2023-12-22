package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Comment;
import com.example.MangaWebsite.Model.Like;
import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTruyenId(Long truyenId);
}
