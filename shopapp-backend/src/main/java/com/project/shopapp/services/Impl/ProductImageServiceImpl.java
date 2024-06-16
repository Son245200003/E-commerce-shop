package com.project.shopapp.services.Impl;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repository.ProductImageRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.services.ProductImageSerivce;
import com.project.shopapp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageSerivce {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    @Override
    public List<ProductImage> findAllImageByProduct(Long id) {
        Product product=productRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Ko tìm thấy product có id = "+id));
        List<ProductImage> productImages=productImageRepository.findByProductId(product);
        return productImages;
    }
}
