package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Review;
import com.example.MangaWebsite.Repository.IReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private IReviewRepository iReviewRepository;
    public List<Review> getAllReviews() {
        return iReviewRepository.findAll();
    }

    public Review update(Review review) {
        return iReviewRepository.save(review);
    }
}
