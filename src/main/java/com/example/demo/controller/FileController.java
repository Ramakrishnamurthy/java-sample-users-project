package com.example.demo.controller;

import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("filename") String filename) {
        return fileService.deleteFile(filename);
    }
}
