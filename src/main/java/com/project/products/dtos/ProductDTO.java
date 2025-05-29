package com.project.products.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private int quantity;
    private int price;
    @JsonIgnore
    private MultipartFile multipartFile;
    private String imageUrl;
}
