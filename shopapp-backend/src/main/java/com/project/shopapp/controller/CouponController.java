package com.project.shopapp.controller;

import com.project.shopapp.response.coupon.CouponCalulateResponse;
import com.project.shopapp.services.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    @GetMapping("/calculate")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> calculateCoupon(
            @RequestParam String couponCode,
            @RequestParam double totalAmout
    ){
        double finalAmount =couponService.caculateCouponValue(couponCode,totalAmout);

        return ResponseEntity.ok(CouponCalulateResponse.builder()
                .result(finalAmount)
                .message("Success sale "+totalAmout+" "+finalAmount).build());
    }
}
