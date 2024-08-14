package com.project.shopapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.response.product.ProductListResponse;
import com.project.shopapp.response.product.ProductResponse;
import com.project.shopapp.services.ProductImageSerivce;
import com.project.shopapp.services.ProductRedisService;
import com.project.shopapp.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final Logger logger= LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final ProductImageSerivce productImageSerivce;
    private final ProductRedisService productRedisService;

    @GetMapping()
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) throws JsonProcessingException {
        int totalPages=0;
        logger.info(keyword+categoryId+page+limit);
        PageRequest pageRequest=PageRequest.of(page,limit, Sort.by("id"));

        List<ProductResponse> productResponses=productRedisService.getAllProducts(keyword,categoryId,pageRequest);
        if (productResponses!=null && !productResponses.isEmpty()) {
            totalPages = productResponses.get(0).getTotalPages();
        }
        if(productResponses==null) {
            Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest);
            totalPages = productPage.getTotalPages();
            productResponses = productPage.getContent();
            for (ProductResponse product : productResponses) {
                product.setTotalPages(totalPages);
            }
            productRedisService.saveAllProducts(productResponses,keyword,categoryId,pageRequest);
        }

        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(productResponses)
                        .totalPage(totalPages)
                .build());
    }
    @GetMapping("{idProduct}")
    public ResponseEntity<?> getProductsById(@PathVariable("idProduct") long idProduct){
//        lấy tổng số trang
            Product product=productService.getProductById(idProduct);
            return ResponseEntity.ok(ProductResponse.fromProduct(product));

    }
    @GetMapping("/imageProduct/{idProduct}")
    public ResponseEntity<?> getImageProduct(@PathVariable("idProduct") long idProduct){
        try {
            List<ProductImage> productImages=productImageSerivce.findAllImageByProduct(idProduct);
            return ResponseEntity.ok(productImages);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping()
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO
            ) throws IOException {
        Product newProduct=productService.createProduct(productDTO);
        return ResponseEntity.ok(newProduct);
    }
    @PostMapping(value = "/upload/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable("id")Long productId,
            @ModelAttribute("files") List<MultipartFile> files) throws IOException {

        Product existingProduct=productService.getProductById(productId);
        //    xử lý ngoại lệ file quá lớn và file ko phải là ảnh

            files=files==null?new ArrayList<MultipartFile>():files;
            List<ProductImage> productImages=new ArrayList<>();
            if(files.size()>ProductImage.MAXIMUM_IMAGES_PRODUCT){
                return ResponseEntity.badRequest().body("Chỉ đc upload 5 ảnh cho 1 sản phẩm");
            }
            for(MultipartFile file:files){
                if(file.getSize()==0){
                    continue;
                }
                if(file.getSize()>10*1024*1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("FILE TO LARGE");
                }
                String contentType=file.getContentType();
                if(contentType==null ||!contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File ko phai la anh");
                }
    //                lưu file và cập nhật thumbnail trong DTO
                String fileName=storeFile(file);
                ProductImage productImage=productService.createProductImage(existingProduct.getId(),
                        ProductImageDTO.builder()
                                .productId(existingProduct.getId())
                                .imageUrl(fileName)
                                .build());
//lưu đối tượng vào database
                productImages.add(productImage);
        }
            return ResponseEntity.ok().body(productImages);
    }
    private String storeFile(MultipartFile file) throws IOException{

        String filename= StringUtils.cleanPath(file.getOriginalFilename());
//        Thêm UUID trước tên để đảm bảo nó là ghi nhất và ko bị ghi đè
        String uniqueFilename= UUID.randomUUID().toString()+"_"+filename;
//        đường dẫn tới thư mục mà bạn muốn lưu file
        Path uploadDir= Paths.get("uploads");
//        kiểm tra và tạo thư mục nếu n ko tồn tại
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
//        đường dẫn đầy đủ den file (đường dẫn đến thư mục uploads, và đường dẫn của file ảnh)
        Path destination=Paths.get(uploadDir.toString(),uniqueFilename);
//        sao chep ảnh vào đường dẫn đích đến file nếu có rồi thì replace
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if(resource.exists()){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }else{
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpg").toUri()));
            }

        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam("ids") String ids) {
        //eg: 1,3,5,7
        try {
            // Tách chuỗi ids thành một mảng các số nguyên
            List<Long> productIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<Product> products = productService.findProductsByIds(productIds);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id,
                                                 @RequestBody ProductDTO productDTO){
        try {
            Product updateProduct= productService.updateProduct(id,productDTO);
            return ResponseEntity.ok(updateProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Update false"+e.getMessage());
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body("Xóa thành công id: "+id);
    }

//    @PostMapping("/generateFakeProduct")
//    public ResponseEntity<String> generateFakeProduct(){
//        Faker faker=new Faker();
//        for (int i=0;i<1_000_000;i++){
//            String productName=faker.commerce().productName();
//            if(productService.existByName(productName)){
//                continue;
//            }
//            ProductDTO productDTO=ProductDTO.builder()
//                    .name(productName)
//                    .price((float)faker.number().numberBetween(10,90_000_000))
//                    .description(faker.lorem().sentence())
//                    .thumbnail("")
//                    .categoryId((long)faker.number().numberBetween(1,4))
//                    .build();
//            try {
//                productService.createProduct(productDTO);
//            }catch (Exception e){
//                return ResponseEntity.badRequest().body(e.getMessage());
//            }
//        }
//    return ResponseEntity.ok("Thanh cing");
//    }
}
