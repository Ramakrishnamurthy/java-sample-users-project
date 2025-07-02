package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private static final String BASE_PATH = "files/";

    public FileService() {
        Path folder = Paths.get(BASE_PATH);
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                throw new RuntimeException("Could not create base directory", e);
            }
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
            return new String(Files.readAllBytes(filePath));
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
}
