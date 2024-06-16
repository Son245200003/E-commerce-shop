package com.project.shopapp.controller;

import com.project.shopapp.dtos.OrderDetailsDTO;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.response.order_detal.OrderDetailResponse;
import com.project.shopapp.services.OrderDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/order_details")

public class OrderDetailController {
    // them moi 1 orderDetails
    @Autowired
    OrderDetailService orderDetailService;
    @PostMapping("")
    public ResponseEntity<?> createOrderDetails(
            @Valid @RequestBody OrderDetailsDTO orderDetailsDTO,
            BindingResult result
    ) {
//        xử lý ngoại lệ validate
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());

                return ResponseEntity.badRequest().body(errorMessage);
            }
            OrderDetail orderDetail=orderDetailService.createOrderDetail(orderDetailsDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") long id){
        OrderDetail orderDetail=orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    }
//    danh sách orderDetails của 1 order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") long orderId){
        List<OrderDetail> orderDetail=orderDetailService.getOrderDetailByOrder(orderId);

        List<OrderDetailResponse> orderDetailResponses=orderDetail
                .stream()
                .map(OrderDetailResponse::fromOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(@Valid @PathVariable("id") long id,
                                                @Valid @RequestBody OrderDetailsDTO orderDetailsDTO
                                                ){
        OrderDetail orderDetail=orderDetailService.updateOrderDetail(id,orderDetailsDTO);
        return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") long id){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete id :"+id);
    }
}
