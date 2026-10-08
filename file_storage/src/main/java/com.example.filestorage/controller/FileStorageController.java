package com.example.filestorage.controller;

import com.example.filestorage.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileStorageController {

    private static final Logger log = LoggerFactory.getLogger(FileStorageController.class);

    private final FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Uploading file: filename={}, size={}", file.getOriginalFilename(), file.getSize());
        fileStorageService.save(file.getOriginalFilename(), file.getBytes());
        log.info("File upload completed: filename={}", file.getOriginalFilename());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename) {
        log.info("Downloading file: filename={}", filename);
        byte[] content = fileStorageService.get(filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        return ResponseEntity.ok().headers(headers).body(content);
    }
}
