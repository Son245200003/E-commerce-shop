package com.project.shopapp.repository;

import com.project.shopapp.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    @Query("select od from OrderDetail od where od.orderId.id=:orderId")
    List<OrderDetail> findByOrderId(@Param("orderId") Long orderId);
}
