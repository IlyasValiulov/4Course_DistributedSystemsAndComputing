package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class FileStorageAsyncService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageAsyncService.class);

    private final RestClient fileStorageRestClient;

    private final Executor fileStorageExecutor;

    public FileStorageAsyncService(
            @Qualifier("fileStorageRestClient") RestClient fileStorageRestClient,
            @Qualifier("fileStorageExecutor") Executor fileStorageExecutor) {

        this.fileStorageRestClient = fileStorageRestClient;
        this.fileStorageExecutor = fileStorageExecutor;
    }

    public CompletableFuture<byte[]> getReport(String filename) {
        String requestId = MDC.get("requestId");

        log.info("Starting asynchronous report download: filename={}", filename);

        return CompletableFuture.supplyAsync(() -> {
            String previousRequestId = MDC.get("requestId");

            try {
                if (requestId != null) {
                    MDC.put("requestId", requestId);
                } else {
                    MDC.remove("requestId");
                }

                log.info("Requesting file from file-storage-service: filename={}",
                        filename);

                byte[] content = fileStorageRestClient.get()
                        .uri("/api/files/{filename}", filename)
                        .retrieve()
                        .body(byte[].class);

                if (content == null) {
                    throw new IllegalStateException(
                            "File storage returned an empty response");
                }

                log.info("Report received: filename={}, size={}",
                        filename, content.length);

                return content;
            } catch (Exception e) {
                log.error("Could not download report: filename={}",
                        filename, e);
                throw new IllegalStateException(
                        "Could not download report: " + filename, e);
            } finally {
                if (previousRequestId != null) {
                    MDC.put("requestId", previousRequestId);
                } else {
                    MDC.remove("requestId");
                }
            }
        }, fileStorageExecutor);
    }
}
