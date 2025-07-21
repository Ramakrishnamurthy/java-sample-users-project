package com.example.demo.controller;

import com.example.demo.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFile() {
        when(fileService.createFile("test.txt", "content")).thenReturn("File created successfully.");
        String result = fileController.createFile("test.txt", "content");
        assertEquals("File created successfully.", result);
    }

    @Test
    void testReadFile() {
        when(fileService.readFile("test.txt")).thenReturn("file content");
        String result = fileController.readFile("test.txt");
        assertEquals("file content", result);
    }

    @Test
    void testDeleteFile() {
        when(fileService.deleteFile("test.txt")).thenReturn("File deleted successfully.");
        String result = fileController.deleteFile("test.txt");
        assertEquals("File deleted successfully.", result);
    }
}
