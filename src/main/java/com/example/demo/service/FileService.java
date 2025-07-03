package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
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
        var filePath = Paths.get(BASE_PATH, filename);
        if (Files.exists(filePath)) {
            return "File already exists.";
        }
        try {
            Files.writeString(filePath, content);
            return "File created successfully.";
        } catch (IOException e) {
            return "Error creating file: " + e.getMessage();
        }
    }

    public String readFile(String filename) {
        var filePath = Paths.get(BASE_PATH, filename);
        if (!Files.exists(filePath)) {
            return "File not found.";
        }
        try {
            return Files.readAllLines(filePath).stream().collect(Collectors.joining("\n", "", "\n"));
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    public String deleteFile(String filename) {
        var filePath = Paths.get(BASE_PATH, filename);
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
        var filePath = Paths.get(BASE_PATH, filename);
        try {
            if (Files.exists(filePath)) {
                return Optional.of(Files.readString(filePath));
            }
        } catch (IOException ignored) {}
        return Optional.empty();
    }
}
