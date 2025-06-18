package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileService {

    private static final String BASE_PATH = "files/";

    public FileService() {
        File folder = new File(BASE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public String createFile(String filename, String content) {
        try {
            File file = new File(BASE_PATH + filename);
            if (file.exists()) {
                return "File already exists.";
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();
            return "File created successfully.";
        } catch (IOException e) {
            return "Error creating file: " + e.getMessage();
        }
    }

    public String readFile(String filename) {
        try {
            File file = new File(BASE_PATH + filename);
            if (!file.exists()) {
                return "File not found.";
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    public String deleteFile(String filename) {
        File file = new File(BASE_PATH + filename);
        if (!file.exists()) {
            return "File not found.";
        }
        if (file.delete()) {
            return "File deleted successfully.";
        } else {
            return "Error deleting file.";
        }
    }
}
