package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UncheckedIOException;

@Service
public class FileService {

    private static final Path BASE_PATH = Paths.get("files");

    public FileService() {
        try {
            if (Files.notExists(BASE_PATH)) {
                Files.createDirectories(BASE_PATH);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create base directory", e);
        }
    }

    public String createFile(String filename, String content) {
        var filePath = BASE_PATH.resolve(filename);
        try {
            if (Files.exists(filePath)) {
                return "File already exists.";
            }
            Files.writeString(filePath, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            return "File created successfully.";
        } catch (IOException e) {
            return "Error creating file: " + e.getMessage();
        }
    }

    public String readFile(String filename) {
        var filePath = BASE_PATH.resolve(filename);
        if (!Files.exists(filePath)) {
            return "File not found.";
        }
        try {
            // Use Files.readString for simple content, or stream for large files
            return Files.readAllLines(filePath, StandardCharsets.UTF_8)
                    .stream().collect(Collectors.joining("\n", "", "\n"));
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    public String deleteFile(String filename) {
        var filePath = BASE_PATH.resolve(filename);
        try {
            if (!Files.exists(filePath)) {
                return "File not found.";
            }
            Files.delete(filePath);
            return "File deleted successfully.";
        } catch (IOException e) {
            return "Error deleting file.";
        }
    }
}
