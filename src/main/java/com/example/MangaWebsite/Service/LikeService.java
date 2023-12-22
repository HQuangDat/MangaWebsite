package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Model.Like;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Repository.ILikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private ILikeRepository ilikeRepository;

    public boolean userLikedTruyen(Truyen truyen, User user) {
        return ilikeRepository.findByTruyenAndUser(truyen, user).isPresent();
    }

    public void toggleLike(Truyen truyen, User user) {
        Optional<Like> existingLike = ilikeRepository.findByTruyenAndUser(truyen, user);

        if (existingLike.isPresent()) {
            ilikeRepository.delete(existingLike.get());
            truyen.setSoLike(truyen.getSoLike()-1);
        } else {
            Like newLike = new Like();
            newLike.setTruyen(truyen);
            newLike.setUser(user);
            ilikeRepository.save(newLike);
            truyen.setSoLike(truyen.getSoLike() + 1);
        }
    }
}