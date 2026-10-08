package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class FileStorageClient {

    private static final Logger log = LoggerFactory.getLogger(FileStorageClient.class);

    private final RestClient fileStorageRestClient;

    public FileStorageClient(@Qualifier("fileStorageRestClient") RestClient fileStorageRestClient) {
        this.fileStorageRestClient = fileStorageRestClient;
    }

    public void upload(String filename, byte[] content) {
        log.info("Uploading file to file-storage-service: filename={}, size={}", filename, content.length);

        ByteArrayResource resource = new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.parseMediaType("text/csv"));

        HttpEntity<ByteArrayResource> filePart = new HttpEntity<>(resource, fileHeaders);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", filePart);

        fileStorageRestClient.post()
                .uri("/api/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        log.info("File uploaded successfully: filename={}", filename);
    }
}
