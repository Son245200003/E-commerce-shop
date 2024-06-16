package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetailsDTO {
    @JsonProperty("order_id")
    @Min(value=1,message = "orderId must be >0")
    private long orderId;

    @JsonProperty("product_id")
    @Min(value=1,message = "productId must be >0")
    private long productId;
    @Min(value=0,message = "productId must be >0")
    private float price;

    @JsonProperty("number_of_products")
    @Min(value=1,message = "numberOfProducts must be >0")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value=0,message = "totalMoney must be >0")
    private float totalMoney;

    private String color;

}
