package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Category;
import com.example.MangaWebsite.Repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;
    public List<Category> getAllCategories(){return categoryRepository.findAll();}
    public Category getCategoryById(Character id){return categoryRepository.findById(id).orElse(null);}
    public Category saveCategory(Category category){return categoryRepository.save(category);}
    public void deleteCategory(Character id){categoryRepository.deleteById(id);}
}
