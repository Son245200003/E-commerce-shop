package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private int quantity;
}
