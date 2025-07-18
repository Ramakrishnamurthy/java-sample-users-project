package com.example.demo.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

public class FileServiceTest {

    private FileService fileService;
    private static final String TEST_FILENAME = "test-file.txt";
    private static final String TEST_CONTENT = "This is test content";

    @Before
    public void setUp() {
        fileService = new FileService();
        // Clean up any existing test file
        try {
            Files.deleteIfExists(Paths.get("files", TEST_FILENAME));
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @After
    public void tearDown() {
        // Clean up test file after each test
        try {
            Files.deleteIfExists(Paths.get("files", TEST_FILENAME));
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @Test
    public void testCreateFile() {
        String result = fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        assertEquals("File created successfully.", result);
        
        assertTrue(Files.exists(Paths.get("files", TEST_FILENAME)));
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
        
        assertEquals(TEST_CONTENT, result);
    }

    @Test
    public void testReadFileNotFound() {
        String result = fileService.readFile("nonexistent.txt");
        
        assertEquals("File not found.", result);
    }

    @Test
    public void testReadFileOptional() {
        // Create file first
        fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        Optional<String> result = fileService.readFileOptional(TEST_FILENAME);
        
        assertTrue(result.isPresent());
        assertEquals(TEST_CONTENT, result.get());
    }

    @Test
    public void testReadFileOptionalNotFound() {
        Optional<String> result = fileService.readFileOptional("nonexistent.txt");
        
        assertFalse(result.isPresent());
    }

    @Test
    public void testFileExists() {
        // Create file first
        fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        assertTrue(fileService.fileExists(TEST_FILENAME));
        assertFalse(fileService.fileExists("nonexistent.txt"));
    }

    @Test
    public void testGetFileSize() {
        // Create file first
        fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        long size = fileService.getFileSize(TEST_FILENAME);
        
        assertTrue(size > 0);
        assertEquals(TEST_CONTENT.length(), size);
    }

    @Test
    public void testGetFileSizeNotFound() {
        long size = fileService.getFileSize("nonexistent.txt");
        
        assertEquals(-1, size);
    }

    @Test
    public void testDeleteFile() {
        // Create file first
        fileService.createFile(TEST_FILENAME, TEST_CONTENT);
        
        String result = fileService.deleteFile(TEST_FILENAME);
        
        assertEquals("File deleted successfully.", result);
        
        assertFalse(Files.exists(Paths.get("files", TEST_FILENAME)));
    }

    @Test
    public void testDeleteFileNotFound() {
        String result = fileService.deleteFile("nonexistent.txt");
        
        assertEquals("File not found.", result);
    }
}