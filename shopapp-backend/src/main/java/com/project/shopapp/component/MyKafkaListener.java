package com.project.shopapp.component;

import com.project.shopapp.models.Category;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@KafkaListener(id = "groupA", topics = {"get-all-categories", "insert-a-category"})
public class MyKafkaListener {

    @KafkaHandler
    public void listenCategory(Category category) {
        System.out.println("Received category: " + category);
        // Xử lý đối tượng Category
    }

    @KafkaHandler
    public void listenListCategories(List<Category> categoryList) {
        System.out.println("Received list of categories: " + categoryList);
        // Xử lý danh sách các đối tượng Category
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Received unknown: " + object);
        // Xử lý khi không nhận diện được định dạng tin nhắn
    }

}
