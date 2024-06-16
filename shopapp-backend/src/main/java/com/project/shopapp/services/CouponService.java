package com.project.shopapp.services;

public interface CouponService {
    double caculateCouponValue(String couponCode,double totalAmount);
}
