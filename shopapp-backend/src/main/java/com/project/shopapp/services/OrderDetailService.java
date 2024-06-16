package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailsDTO;
import com.project.shopapp.models.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    OrderDetail createOrderDetail(OrderDetailsDTO orderDetailsDTO);

    OrderDetail getOrderDetail(long id);
    OrderDetail updateOrderDetail(long id,OrderDetailsDTO orderDetailsDTO);
    void deleteOrderDetail(long id);
    List<OrderDetail> getOrderDetailByOrder(Long orderId);
}
