package com.project.shopapp.response.coupon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CouponCalulateResponse {
    @JsonProperty("result")
    private Double result;

    @JsonProperty("message")
    private String message;



}
