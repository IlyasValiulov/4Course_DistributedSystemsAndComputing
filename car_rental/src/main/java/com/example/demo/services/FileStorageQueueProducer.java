package com.example.demo.services;

import com.example.demo.config.RabbitMqConfig;
import com.example.demo.dto.FileUploadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.MDC;

@Service
public class FileStorageQueueProducer {

    private static final Logger log = LoggerFactory.getLogger(FileStorageQueueProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public FileStorageQueueProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String filename, byte[] content) {
        String requestId = MDC.get("requestId");
        FileUploadMessage message = new FileUploadMessage(filename, content, requestId);

        log.info(
                "Sending file to RabbitMQ: queue={}, filename={}, size={}, requestId={}",
                RabbitMqConfig.FILE_STORAGE_QUEUE,
                filename,
                content.length,
                requestId
        );

        rabbitTemplate.convertAndSend(RabbitMqConfig.FILE_STORAGE_QUEUE, message);

        log.info(
                "File sent to RabbitMQ successfully: filename={}, requestId={}",
                filename,
                requestId
        );
    }
}
