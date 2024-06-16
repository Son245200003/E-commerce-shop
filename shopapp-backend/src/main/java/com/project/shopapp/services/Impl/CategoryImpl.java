package com.project.shopapp.services.Impl;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.repository.CategoryRepository;
import com.project.shopapp.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category creatCategory(CategoryDTO categoryDTO) {
        Category newCategory= Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(()->new RuntimeException("Category not found with id:"+id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(long id, CategoryDTO categoryDTO) {
        Category existingCategory=getCategoryById(id);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.saveAndFlush(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
