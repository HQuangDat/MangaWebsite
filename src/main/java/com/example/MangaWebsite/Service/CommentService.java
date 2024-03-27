package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Comment;
import com.example.MangaWebsite.Repository.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentService {
    @Autowired
    private ICommentRepository iCommentRepository;

    public Comment update(Comment cmt) {
        return iCommentRepository.save(cmt);
    }

    public List<Comment> getAllComment(Long truyen_id) {
        return iCommentRepository.findAllByTruyenId(truyen_id);
    }
}
