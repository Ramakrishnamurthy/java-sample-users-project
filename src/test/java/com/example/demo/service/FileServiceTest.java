package com.example.demo.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FileServiceTest {

    private FileService fileService;
    private static final String TEST_FILENAME = "test-file.txt";
    private static final String TEST_CONTENT = "This is test content";

    @BeforeEach
    public void setUp() {
        fileService = new FileService();
        // Clean up any existing test file
        File testFile = new File("files/" + TEST_FILENAME);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @AfterEach
    public void tearDown() {
        // Clean up test file after each test
        File testFile = new File("files/" + TEST_FILENAME);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testCreateFile() {
        String result = fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        assertEquals("File created successfully.", result);
        
        File file = new File("files/" + TEST_FILENAME);
        assertTrue(file.exists());
    }

    @Test
    public void testCreateFileAlreadyExists() {
        // Create file first
        fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        // Try to create same file again
        String result = fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        assertEquals("File already exists.", result);
    }

    @Test
    public void testReadFile() {
        // Create file first
        fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        String result = fileService.readFile(TEST_FILENAME);
        
        assertEquals(TEST_CONTENT + "\n", result);
    }

    @Test
    public void testReadFileNotFound() {
        String result = fileService.readFile("nonexistent.txt");
        
        assertEquals("File not found.", result);
    }

    @Test
    public void testDeleteFile() {
        // Create file first
        fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        String result = fileService.deleteFile(TEST_FILENAME);
        
        assertEquals("File deleted successfully.", result);
        
        File file = new File("files/" + TEST_FILENAME);
        assertFalse(file.exists());
    }

    @Test
    public void testDeleteFileNotFound() {
        String result = fileService.deleteFile("nonexistent.txt");
        
        assertEquals("File not found.", result);
    }
}