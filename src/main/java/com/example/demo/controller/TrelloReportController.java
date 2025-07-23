package com.example.demo.controller;

import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trello")
public class TrelloReportController {

    @Autowired
    private TrelloBoardReportService trelloBoardReportService;

    @GetMapping("/generate-report")
    public String generateAgileReport(@RequestParam String boardId) {
        return trelloBoardReportService.exportBoardAndLists(boardId);
    }
    @GetMapping("/generate-cards-report")
    public String generateCardsReport(@RequestParam String boardId) {
        return trelloBoardReportService.exportCardsOverview(boardId);
    }

    @GetMapping("/generate-members-report")
    public String generateMembersReport(@RequestParam String boardId) {
        return trelloBoardReportService.exportMembersParticipation(boardId);
    }

    @GetMapping("/generate-checklists-report")
    public String generateChecklistReport(@RequestParam String boardId) {
        return trelloBoardReportService.exportChecklistReport(boardId);
    }

    @GetMapping("/generate-actions-report")
    public String generateActionsReport(@RequestParam String boardId) {
        return trelloBoardReportService.exportBoardActionsReport(boardId);
    }

    @GetMapping("/generate-labels-report")
    public String generateLabelsReport(@RequestParam String boardId) {
        return trelloBoardReportService.exportLabelUsageReport(boardId);
    }
}
