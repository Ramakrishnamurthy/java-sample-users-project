package com.example.demo.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private FileService fileService;
    private String testFileName;

    @BeforeEach
    void setUp() {
        fileService = new FileService();
        testFileName = "testfile-" + UUID.randomUUID() + ".txt";
        // Clean up before test in case of leftover
        File file = new File("files/" + testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    void tearDown() {
        File file = new File("files/" + testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testCreateFile() {
        String result = fileService.createFile(testFileName, "Hello World");
        assertEquals("File created successfully.", result);
    }

    @Test
    void testCreateFileAlreadyExists() {
        fileService.createFile(testFileName, "Hello World");
        String result = fileService.createFile(testFileName, "Hello Again");
        assertEquals("File already exists.", result);
    }

    @Test
    void testReadFile() {
        fileService.createFile(testFileName, "Hello World");
        String content = fileService.readFile(testFileName);
        assertNotNull(content);
        assertTrue(content.contains("Hello World"), "File content should contain 'Hello World', but was: " + content);
    }

    @Test
    void testReadFileNotFound() {
        String result = fileService.readFile("nonexistent-" + UUID.randomUUID() + ".txt");
        assertEquals("File not found.", result);
    }

    @Test
    void testDeleteFile() {
        fileService.createFile(testFileName, "Hello World");
        String result = fileService.deleteFile(testFileName);
        assertEquals("File deleted successfully.", result);
    }

    @Test
    void testDeleteFileNotFound() {
        String result = fileService.deleteFile("nonexistent-" + UUID.randomUUID() + ".txt");
        assertEquals("File not found.", result);
    }
}