package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;

import java.util.List;

public interface CategoryService {
    Category creatCategory(CategoryDTO categoryd);

    Category getCategoryById(long id);
    List<Category> getAllCategories();

    Category updateCategory(long id, CategoryDTO category);

    void deleteCategory(long id);
}
