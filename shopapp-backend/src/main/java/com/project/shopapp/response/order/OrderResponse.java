package com.project.shopapp.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.response.BaseResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter@Getter
public class OrderResponse extends BaseResponse {
    private long id;
    @JsonProperty("user_id")
    private Long userId;
//    @JsonProperty("user_id")
//    private User userId;
    @JsonProperty("fullname")
    private String fullName;
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("address")
    private String address;
    @JsonProperty("note")
    private String note;
    @JsonProperty("order_date")
    private LocalDate orderDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("order_details")
    private List<OrderDetail> orderDetails;

    public static OrderResponse fromOrder(Order order){
        LocalDate orderDate = convertToLocalDate(order.getOrderDate());
        OrderResponse orderResponse=OrderResponse
                .builder()
                .id(order.getId())
                .userId(order.getUserId().getId())
                .fullName(order.getFullName())
                .phoneNumber(order.getPhoneNumber())
                .email(order.getEmail())
                .address(order.getAddress())
                .orderDate(orderDate)
                .shippingDate(order.getShippingDate())
                .status(order.getStatus())
                .note(order.getNote())
                .paymentMethod(order.getPaymentMethod())
                .orderDetails(order.getOrderDetails())
                .build();
        return orderResponse;
    }
    private static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
