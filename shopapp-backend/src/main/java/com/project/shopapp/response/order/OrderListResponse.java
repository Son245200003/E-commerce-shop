package com.project.shopapp.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class OrderListResponse {
    private List<OrderResponse> orders;
    private int totalPages;
}
