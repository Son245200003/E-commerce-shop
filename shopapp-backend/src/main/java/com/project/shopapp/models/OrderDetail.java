package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order orderId;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price")
    private Float price;

    @Column(name = "number_of_products",nullable = false)
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Column(name = "total_money")
    @JsonProperty("total_money")
    private Float totalMoney;

    @Column(name = "color")
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    @JsonBackReference
    private Coupon coupon;
}
