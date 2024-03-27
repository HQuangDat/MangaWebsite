package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Comment;
import com.example.MangaWebsite.Model.Like;
import com.example.MangaWebsite.Model.Truyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTruyenId(Long truyenId);
}
