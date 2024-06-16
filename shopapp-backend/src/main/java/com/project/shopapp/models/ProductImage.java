package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
    @Table(name = "product_images")
public class ProductImage {
    public static final int MAXIMUM_IMAGES_PRODUCT=5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product productId;

    @Column(name = "image_url",length = 300)
    @JsonProperty("image_url")
    private String imageUrl;
}
