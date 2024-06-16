package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductImageDTO {

    @JsonProperty("product_id")
    @Min(value = 0,message = "Product id must be >0 ")
    private Long productId;

    @Size(min = 5,max = 200,message = "image name")
    @JsonProperty("image_url")
    private String imageUrl;
}
