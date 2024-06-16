package com.project.shopapp.controller;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.models.Order;
import com.project.shopapp.response.order.OrderListResponse;
import com.project.shopapp.response.order.OrderResponse;
import com.project.shopapp.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/orders")

public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrderByUser(@PathVariable("user_id") long userId){
        try {
            return ResponseEntity.ok(orderService.getAllOrdersByUser(userId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") long orderId){
        try {
            OrderResponse orderResponse=orderService.getOrderById(orderId);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping(value = "")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
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
            Order order=orderService.createOrder(orderDTO);

           return ResponseEntity.ok(OrderResponse.fromOrder(order));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") long id,@Valid @RequestBody OrderDTO orderDTO){
        OrderResponse existing=orderService.updateOrder(id,orderDTO);

        return ResponseEntity.ok(existing);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") long id){

        orderService.deleteOrder(id);
            return ResponseEntity.ok("Xóa mềm đơn hàng thaành công");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-orders-by-keyword")
    public ResponseEntity<OrderListResponse> getOrderByKeyword(
            @RequestParam(defaultValue = "") String keyword,
             @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int limit){
        PageRequest pageRequest=PageRequest.of(page,limit, Sort.by("id"));
        Page<OrderResponse> orderPage=orderService.getOrderByKeyword(keyword,pageRequest)
                .map(OrderResponse::fromOrder);
        int totalPage=orderPage.getTotalPages();
        List<OrderResponse> orderResponses=orderPage.getContent();
        return ResponseEntity.ok(OrderListResponse.builder()
                        .orders(orderResponses)
                        .totalPages(totalPage)
                .build());
    }
}
