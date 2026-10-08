package com.example.filestorage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private final Path storagePath;

    public FileStorageService(@Value("${file.storage.path}") String storagePath) {
        this.storagePath = Path.of(storagePath);
        try {
            Files.createDirectories(this.storagePath);
            log.info("File storage initialized: path={}", this.storagePath);
        } catch (IOException e) {
            log.error("Could not create file storage directory: path={}", this.storagePath, e);
            throw new IllegalStateException("Could not create file storage directory", e);
        }
    }

    public void save(String filename, byte[] content) {
        log.info("Saving file: filename={}, size={}", filename, content.length);
        try {
            Path file = storagePath.resolve(filename);
            Files.write(file, content);
            log.info("File saved successfully: filename={}, path={}", filename, file);
        } catch (IOException e) {
            log.error("Could not save file: filename={}", filename, e);
            throw new IllegalStateException("Could not save file: " + filename, e);
        }
    }

    public byte[] get(String filename) {
        log.info("Reading file: filename={}", filename);
        try {
            Path file = storagePath.resolve(filename);
            byte[] content = Files.readAllBytes(file);
            log.info("File read successfully: filename={}, size={}", filename, content.length);
            return content;
        } catch (IOException e) {
            log.error("Could not read file: filename={}", filename, e);
            throw new IllegalStateException("Could not read file: " + filename, e);
        }
    }
}
