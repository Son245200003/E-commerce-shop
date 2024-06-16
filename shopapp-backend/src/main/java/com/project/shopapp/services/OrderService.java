package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.response.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO);
    OrderResponse getOrderById(long id);
    OrderResponse updateOrder(long id, OrderDTO orderDTO);

    void deleteOrder(Long id);
    List<Order> getAllOrders();
    List<Order> getAllOrdersByUser(Long userId);
    Page<Order> getOrderByKeyword(String keyword, Pageable pageable);
}
