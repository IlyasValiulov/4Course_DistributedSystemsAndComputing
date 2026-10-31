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
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        log.info("Download request received: filename={}", filename);
        byte[] content = fileStorageService.get(filename);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(filename)
                                .build()
                                .toString()
                )
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(content);
    }
}
