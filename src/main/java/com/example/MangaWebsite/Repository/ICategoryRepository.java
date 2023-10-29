package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ICategoryRepository extends JpaRepository<Category, Long> {
}
