package com.example.demo.controller;

import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trello")
public class TrelloReportController {

    @Autowired
    private TrelloReportService trelloReportService;

    @GetMapping("/generate-report")
    public String generateAgileReport(@RequestParam String boardId) {
        return trelloReportService.generateReport(boardId);
    }
}
