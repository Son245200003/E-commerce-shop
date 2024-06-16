package com.project.shopapp.repository;

import com.project.shopapp.models.Order;
import com.project.shopapp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select od from Order od where od.userId.id=:userId")
    List<Order> findByUserId(@Param("userId") Long userId);
    @Query("SELECT o FROM Order o WHERE o.active=true " +
            "AND (:keyword IS NULL OR :keyword ='' OR o.fullName LIKE %:keyword% OR o.address LIKE %:keyword% " +
            "OR o.note LIKE %:keyword%)")
    Page<Order> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
