package com.example.filestorage.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String FILE_STORAGE_QUEUE = "file-storage-queue";

    @Bean
    public Queue fileStorageQueue() {
        return new Queue(FILE_STORAGE_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
