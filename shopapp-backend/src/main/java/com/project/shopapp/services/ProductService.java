package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.response.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {
    public Product createProduct(ProductDTO productDTO);

    Product getProductById(long id);

    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId, PageRequest pageRequest);

    Product updateProduct(long id,ProductDTO productDTO);
    void deleteProduct(long id);
    boolean existByName(String name);
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO);

    List<Product> findProductsByIds(List<Long> productIds);
}
