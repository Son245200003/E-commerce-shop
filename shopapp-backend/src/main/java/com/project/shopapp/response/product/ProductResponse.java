package com.project.shopapp.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.response.BaseResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponse extends BaseResponse {
    private long id;
    private String name;
    private float price;
    private String thumbnail;
    private String description;
    private int totalPages;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();
    public static ProductResponse fromProduct(Product product){
        ProductResponse productReponse= ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategoryId().getId())
                .productImages(product.getProductImages())
                .build();
        productReponse.setCreatedAt(product.getCreatedAt());
        productReponse.setUpdatedAt(product.getUpdatedAt());
        return productReponse;
    }
}
