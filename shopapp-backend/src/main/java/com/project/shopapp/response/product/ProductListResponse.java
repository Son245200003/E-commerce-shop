package com.project.shopapp.response.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ProductListResponse {
    private List<ProductResponse> products;
    @JsonProperty("totalPages")
    private int totalPage;
}
