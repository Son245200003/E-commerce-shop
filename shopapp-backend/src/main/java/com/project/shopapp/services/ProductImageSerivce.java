package com.project.shopapp.services;

import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;

import java.util.List;

public interface ProductImageSerivce {
    public List<ProductImage> findAllImageByProduct(Long idProduct);
}
