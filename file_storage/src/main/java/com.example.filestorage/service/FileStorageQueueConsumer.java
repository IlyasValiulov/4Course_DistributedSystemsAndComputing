package com.example.filestorage.service;

import com.example.filestorage.config.RabbitMqConfig;
import com.example.filestorage.dto.FileUploadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class FileStorageQueueConsumer {

    private static final Logger log = LoggerFactory.getLogger(FileStorageQueueConsumer.class);

    private final FileStorageService fileStorageService;

    public FileStorageQueueConsumer(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @RabbitListener(queues = RabbitMqConfig.FILE_STORAGE_QUEUE)
    public void receive(FileUploadMessage message) {
        String requestId = message.getRequestId();

        if (requestId != null) {
            MDC.put("requestId", requestId);
        }

        try {
            log.info(
                    "Received file from RabbitMQ: queue={}, filename={}, size={}, requestId={}",
                    RabbitMqConfig.FILE_STORAGE_QUEUE,
                    message.getFilename(),
                    message.getContent().length,
                    requestId
            );

            fileStorageService.save(message.getFilename(), message.getContent());

            log.info(
                    "File stored successfully: filename={}, requestId={}",
                    message.getFilename(),
                    requestId
            );

        } finally {
            if (requestId != null) {
                MDC.remove("requestId");
            }
        }
    }
}
