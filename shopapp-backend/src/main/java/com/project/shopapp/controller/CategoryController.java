package com.project.shopapp.controller;

import com.project.shopapp.component.converts.CategoryMessageConverter;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.response.category.UpdateCategoryResponse;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;


import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
   private final LocalizationUtils localizationUtils;
   private final KafkaTemplate<String,Object> kafkaTemplate;

    @GetMapping()
//    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllCategories(){
        List<Category> categories=categoryService.getAllCategories();

        this.kafkaTemplate.send("get-all-categories",categories);
        this.kafkaTemplate.setMessageConverter(new CategoryMessageConverter());
        return ResponseEntity.ok(categories);
    }
    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryDetail(@PathVariable long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
    @PostMapping()
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Category category=categoryService.creatCategory(categoryDTO);
        this.kafkaTemplate.send("insert-a-category",category);
        this.kafkaTemplate.setMessageConverter(new CategoryMessageConverter());
        return ResponseEntity.ok(category);
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id
            ,@Valid @RequestBody CategoryDTO categoryDTO
            ,BindingResult result
            ){
        if (result.hasErrors()) {
            List<String> errorMessage = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errorMessage);
        }
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok(UpdateCategoryResponse.builder()
                .message(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_SUCCESSFULLY))
                .build());
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete Category with id ="+id+" Sucessfully");
    }
}
