package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    private static final String BASE_PATH = "files/";

    public FileService() {
        try {
            Files.createDirectories(Paths.get(BASE_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create base directory", e);
        }
    }

    public String createFile(String filename, String content) {
        Path filePath = Paths.get(BASE_PATH, filename);
        
        if (Files.exists(filePath)) {
            return "File already exists.";
        }
        
        try {
            Files.write(filePath, content.getBytes());
            return "File created successfully.";
        } catch (IOException e) {
            return "Error creating file: " + e.getMessage();
        }
    }

    public String readFile(String filename) {
        Path filePath = Paths.get(BASE_PATH, filename);
        
        if (!Files.exists(filePath)) {
            return "File not found.";
        }
        
        try {
            return Files.lines(filePath)
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    public String deleteFile(String filename) {
        Path filePath = Paths.get(BASE_PATH, filename);
        
        if (!Files.exists(filePath)) {
            return "File not found.";
        }
        
        try {
            Files.delete(filePath);
            return "File deleted successfully.";
        } catch (IOException e) {
            return "Error deleting file: " + e.getMessage();
        }
    }

    public Optional<String> readFileOptional(String filename) {
        Path filePath = Paths.get(BASE_PATH, filename);
        
        if (!Files.exists(filePath)) {
            return Optional.empty();
        }
        
        try {
            String content = Files.lines(filePath)
                    .collect(Collectors.joining("\n"));
            return Optional.of(content);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean fileExists(String filename) {
        return Files.exists(Paths.get(BASE_PATH, filename));
    }

    public long getFileSize(String filename) {
        Path filePath = Paths.get(BASE_PATH, filename);
        try {
            return Files.size(filePath);
        } catch (IOException e) {
            return -1;
        }
    }
}
