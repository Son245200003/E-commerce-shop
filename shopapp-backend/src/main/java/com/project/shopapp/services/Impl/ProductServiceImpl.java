package com.project.shopapp.services.Impl;

import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Exception.InvalidParamException;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.response.product.ProductResponse;
import com.project.shopapp.repository.CategoryRepository;
import com.project.shopapp.repository.ProductImageRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }
    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        Category existing=categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Ko tìm thấy id :"+productDTO.getCategoryId()));
        Product newProduct=Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .categoryId(existing)
                .description(productDTO.getDescription())
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
                "Ko tìm thấy product với id ="+id
        ));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId, PageRequest pageRequest) {
        // Lấy danh sách sản phẩm theo trang (page), giới hạn (limit), và categoryId (nếu có)
        Page<Product> productsPage;
        productsPage = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO) {
        Product existingProduct=getProductById(id);
        if(existingProduct!=null){
            Category existingCategory=categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Ko tìm thấy id :"+productDTO.getCategoryId()));

            if (productDTO.getName() != null) {
                existingProduct.setName(productDTO.getName());
            }
            if (productDTO.getDescription() != null) {
                existingProduct.setDescription(productDTO.getDescription());
            }
            if (productDTO.getPrice() != 0) {
                existingProduct.setPrice(productDTO.getPrice());
            }
            // Update category only if the new value is not null
            if (productDTO.getCategoryId() != null) {
                existingProduct.setCategoryId(existingCategory);
            }
            if (productDTO.getThumbnail() != null) {
                existingProduct.setThumbnail(productDTO.getThumbnail());
            }
            return productRepository.saveAndFlush(existingProduct);
        }

        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Optional<Product> product=productRepository.findById(id);
        if(product.isPresent()){
            productRepository.deleteById(id);
        }
    }

    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }
    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO){
        Product existingProduct=productRepository.findById(productImageDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("KO tìm thấy sản phẩm with id: "+productImageDTO.getProductId()));
        ProductImage newProductImage=ProductImage.builder()
                .productId(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
// ko cho insert quá 5 ảnh cho 1 sản phẩm
        int size=productImageRepository.findByProductId(existingProduct).size();
        if(size>=ProductImage.MAXIMUM_IMAGES_PRODUCT){
            throw new InvalidParamException("1 product chỉ thêm tối đa 5 ảnh");
        }
        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }
}
