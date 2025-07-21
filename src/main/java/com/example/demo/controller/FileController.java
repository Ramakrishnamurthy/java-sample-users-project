package com.example.demo.controller;

import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
        return Optional.ofNullable(fileService.createFile(filename, content)).orElse("");
    }

    @GetMapping("/read")
    public String readFile(@RequestParam("filename") String filename) {
        return Optional.ofNullable(fileService.readFile(filename)).orElse("");
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("filename") String filename) {
        return Optional.ofNullable(fileService.deleteFile(filename)).orElse("");
    }
}
