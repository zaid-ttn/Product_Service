package com.project.products.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig{


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("product.exchange");
    }


    @Bean
    public Queue productUpsertQueue() {
        return new Queue("product.upsert.queue");
    }

    @Bean
    public Queue productDeleteQueue() {
        return new Queue("product.delete.queue");
    }


    @Bean
    public Binding upsertBinding(Queue productUpsertQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productUpsertQueue).to(productExchange).with("product.upsert.queue");
    }

    @Bean
    public Binding deleteBinding(Queue productDeleteQueue, DirectExchange productExchange) {
        return BindingBuilder.bind(productDeleteQueue).to(productExchange).with("product.delete.queue");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
