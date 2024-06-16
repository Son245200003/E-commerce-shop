package com.project.shopapp.services.Impl;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.dtos.OrderDetailsDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repository.OrderDetailRepository;
import com.project.shopapp.repository.OrderRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailsDTO orderDetailsDTO) {
        Order order=orderRepository.findById(orderDetailsDTO.getOrderId()).orElseThrow(() ->
                new DataNotFoundException("Ko tim thay Order:"+orderDetailsDTO.getOrderId()));
        Product product=productRepository.findById(orderDetailsDTO.getProductId()).orElseThrow(() ->
                new DataNotFoundException("Ko tim thay Order:"+orderDetailsDTO.getProductId()));
        OrderDetail orderDetail=OrderDetail.builder()
                .orderId(order)
                .color(orderDetailsDTO.getColor())
                .price(orderDetailsDTO.getPrice())
                .product(product)
                .numberOfProducts(orderDetailsDTO.getNumberOfProducts())
                .totalMoney(orderDetailsDTO.getTotalMoney())
                .build();

        orderDetailRepository.save(orderDetail);
        return orderDetail;

    }

    @Override
    public OrderDetail getOrderDetail(long id) {
        return orderDetailRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Ko tim thay OrderDetail:"+id));
    }

    @Override
    @Transactional
    public OrderDetail updateOrderDetail(long id, OrderDetailsDTO orderDetailsDTO) {
        OrderDetail existingOrderDetail=getOrderDetail(id);
        Order existingOrder=orderRepository.findById(orderDetailsDTO.getOrderId()).orElseThrow(() ->
                new DataNotFoundException("Ko tim thay OrderDetail:"+orderDetailsDTO.getOrderId()));
        Product existingProduct=productRepository.findById(orderDetailsDTO.getProductId()).orElseThrow(() ->
                new DataNotFoundException("Ko tim thay Order:"+orderDetailsDTO.getProductId()));
        if(existingOrderDetail!=null){
            existingOrderDetail.setOrderId(existingOrder);
            existingOrderDetail.setProduct(existingProduct);
            existingOrderDetail.setPrice(orderDetailsDTO.getPrice());
            existingOrderDetail.setColor(orderDetailsDTO.getColor());
            existingOrderDetail.setNumberOfProducts(orderDetailsDTO.getNumberOfProducts());
            existingOrderDetail.setTotalMoney(orderDetailsDTO.getTotalMoney());
        }
        return orderDetailRepository.saveAndFlush(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrder(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
