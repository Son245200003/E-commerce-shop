package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1,message = "Userid must be >0")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "phoneNumber ko đc để trống")
    @Size(min = 5,message = "Phone number tối thiểu 5 kí tự")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ ko đc rỗng")
    @JsonProperty("address")
    private String address;

    private String note;
    private String status;
    @JsonProperty("total_money")
    @Min(value=0,message = "Total money must be >0")
    private float totalMoney;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItemDTOS;
}
