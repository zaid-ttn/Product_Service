package com.project.products.config;

import com.project.products.model.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PublishEvent {
    private Logger logger = LoggerFactory.getLogger(PublishEvent.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendProductUpsert(Product product) {
        rabbitTemplate.convertAndSend("product.exchange","product.upsert.queue",product);
        logger.info("Product upsert successfully");
    }

    public void sendProductDelete(Long productId) {
        rabbitTemplate.convertAndSend("product.exchange","product.delete.queue", productId);
    }
}
