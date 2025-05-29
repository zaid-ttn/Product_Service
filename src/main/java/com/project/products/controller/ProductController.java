package com.project.products.controller;

import com.project.products.dtos.ProductDTO;
import com.project.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/upload/product")
    public ResponseEntity<String> uploadProduct(@Valid @ModelAttribute ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return ResponseEntity.ok("Product saved successfully");
    }

    @GetMapping("/list/product/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        ProductDTO productDTO=productService.getProduct(id);
        return ResponseEntity.ok(productDTO);
    }
    @GetMapping("/list/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTO=productService.getAllProduct();
        return ResponseEntity.ok(productDTO);
    }
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}

