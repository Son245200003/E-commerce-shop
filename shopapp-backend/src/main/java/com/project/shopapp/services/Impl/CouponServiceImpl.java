package com.project.shopapp.services.Impl;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.models.Coupon;
import com.project.shopapp.models.CouponCondition;
import com.project.shopapp.repository.CouponConditionRepository;
import com.project.shopapp.repository.CouponRepository;
import com.project.shopapp.services.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponConditionRepository couponConditionRepository;
    @Override
    public double caculateCouponValue(String couponCode, double totalAmount) {
        Coupon coupon=couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new DataNotFoundException("Not found Coupin with code: "+couponCode));
        if (!coupon.isActive()){
            return totalAmount;
        }
        double discount=caculateDiscount(coupon,totalAmount);
        double finalAmount=totalAmount-discount;
        return finalAmount;
    }
    private double caculateDiscount(Coupon coupon, double totalAmount){
        List<CouponCondition> conditions=couponConditionRepository.findByCouponId(coupon.getId());
        double discount=0.0;
        double updateTotalAmount=totalAmount;
        for (CouponCondition condition:conditions){
            String attribute=condition.getAttribute();
            String operator=condition.getOperator();
            String value=condition.getValue();
            double percentDiscount=Double.parseDouble(String.valueOf(condition.getDiscountAmount()));
            if (attribute.equals("minium_amount")){
                if (operator.equals(">")&& updateTotalAmount>Double.parseDouble(value)){
                    discount+=updateTotalAmount * percentDiscount /100 ;
                }
            }else if (attribute.equals("applicable_date")){
                LocalDate applicabDate=LocalDate.parse(value);
                LocalDate currentDate=LocalDate.now();
                if(operator.equals("BETWEEN")&& currentDate.isEqual(applicabDate)){
                    discount+=updateTotalAmount *percentDiscount /100;
                }
            }
            updateTotalAmount=updateTotalAmount-discount;

        }
        return discount;
    }
}
