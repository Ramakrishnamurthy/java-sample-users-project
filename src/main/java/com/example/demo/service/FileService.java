package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;

@Service
public class FileService {

    private static final Path BASE_PATH = Paths.get("files");

    public FileService() {
        try {
            Files.createDirectories(BASE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Could not create base directory", e);
        }
    }

    public String createFile(String filename, String content) {
        Path file = BASE_PATH.resolve(filename);
        if (Files.exists(file)) {
            return "File already exists.";
        }
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(content);
            return "File created successfully.";
        } catch (IOException e) {
            return "Error creating file: " + e.getMessage();
        }
    }

    public String readFile(String filename) {
        Path file = BASE_PATH.resolve(filename);
        if (!Files.exists(file)) {
            return "File not found.";
        }
        try {
            return Files.readString(file);
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    public String deleteFile(String filename) {
        Path file = BASE_PATH.resolve(filename);
        try {
            boolean deleted = Files.deleteIfExists(file);
            return deleted ? "File deleted successfully." : "File not found.";
        } catch (IOException e) {
            return "Error deleting file: " + e.getMessage();
        }
    }
}
