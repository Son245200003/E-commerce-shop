package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    @NotBlank(message = "Title required")
    @Size(min = 3,max = 200,message = "Tên của sản phẩm từ 3 kí tự đến 200 kí tu")
    private String name;
    @Min(value = 0,message = "Price must be > 0")
    @Max(value = 10000000,message = "Price must be < 10,000,000")
    private float price;
    private String thumbnail;
    private String description;
    private Long colorId;
    @JsonProperty("category_id")// nhận từ phía client tên là category_id còn dùng trong server thì là categoryId
    private Long categoryId;

//    private List<MultipartFile> files;
}
