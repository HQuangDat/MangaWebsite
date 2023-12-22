package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReviewRepository extends JpaRepository<Review, Long> {

}
