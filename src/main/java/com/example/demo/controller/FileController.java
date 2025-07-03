package com.example.demo.controller;

import com.example.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/create")
    public String createFile(@RequestParam("filename") String filename,
                             @RequestParam("content") String content) {
        return fileService.createFile(filename, content);
    }

    @GetMapping("/read")
    public String readFile(@RequestParam("filename") String filename) {
        return fileService.readFile(filename);
    }

    @GetMapping("/read-optional")
    public ResponseEntity<String> readFileOptional(@RequestParam("filename") String filename) {
        return fileService.readFileOptional(filename)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("filename") String filename) {
        return fileService.deleteFile(filename);
    }

    @GetMapping("/exists")
    public boolean fileExists(@RequestParam("filename") String filename) {
        return fileService.fileExists(filename);
    }

    @GetMapping("/size")
    public ResponseEntity<Long> getFileSize(@RequestParam("filename") String filename) {
        long size = fileService.getFileSize(filename);
        return size >= 0 ? ResponseEntity.ok(size) : ResponseEntity.notFound().build();
    }
}
