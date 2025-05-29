package com.project.products.service;

import com.project.products.config.PublishEvent;
import com.project.products.dtos.OrderDTO;
import com.project.products.dtos.ProductDTO;
import com.project.products.model.Product;
import com.project.products.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    S3ImageUploader s3ImageUploader;

    @Autowired
    PublishEvent productEventPublisher;

    public void saveProduct(ProductDTO productDTO) {
        logger.info("Saving product");
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(s3ImageUploader.uploadImage(productDTO.getMultipartFile()));
        product.setQuantity(productDTO.getQuantity());
        productEventPublisher.sendProductUpsert(product);
        productRepository.save(product);
        logger.info("Product saved");
    }
    public void deleteProduct(Long id) {
        logger.info("Deleting product");
        productRepository.deleteById(id);
        productEventPublisher.sendProductDelete(id);
        logger.info("Product deleted");
    }

    @RabbitListener(queues = "order.queue")
    public void orderPlaced(OrderDTO orderDTO) {
        logger.info("Updating quantity");
        Product product =productRepository.findById(orderDTO.getId()).
                orElseThrow(()-> new RuntimeException("Product not found"));
        logger.info("Product Found");
        product.setQuantity(product.getQuantity() - orderDTO.getQuantity());
        productRepository.save(product);
        logger.info("Product updated");

    }

    public ProductDTO getProduct(Long id) {
        logger.info("Getting product");
        Product product=productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        ProductDTO productDTO=new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setImageUrl(s3ImageUploader.preSignedUrl(product.getImageUrl()));
        logger.info("Product found");
        return productDTO;
    }

    public List<ProductDTO> getAllProduct() {
        logger.info("Getting all products");
        List<ProductDTO> productDTOList=new ArrayList<>();
        List<Product> products=productRepository.findAll();
        for(Product product:products){
            ProductDTO productDTO=new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(product.getQuantity());
            productDTO.setImageUrl(s3ImageUploader.preSignedUrl(product.getImageUrl()));
            productDTOList.add(productDTO);
        }
        logger.info("Products found");
        return productDTOList;
    }
}
