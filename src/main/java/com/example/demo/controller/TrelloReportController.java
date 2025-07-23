package com.example.demo.controller;

import com.example.demo.service.TrelloBoardReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trello")
public class TrelloReportController {

    @Autowired
    private TrelloBoardReportService trelloBoardReportService;

    @RequestMapping(value = "/generate-report", method = RequestMethod.GET)
    public String generateAgileReport(@RequestParam("boardId") String boardId) {
        return trelloBoardReportService.exportBoardAndLists(boardId);
    }

    @RequestMapping(value = "/generate-cards-report", method = RequestMethod.GET)
    public String generateCardsReport(@RequestParam("boardId") String boardId) {
        return trelloBoardReportService.exportCardsOverview(boardId);
    }

    @RequestMapping(value = "/generate-members-report", method = RequestMethod.GET)
    public String generateMembersReport(@RequestParam("boardId") String boardId) {
        return trelloBoardReportService.exportMembersParticipation(boardId);
    }

    @RequestMapping(value = "/generate-checklists-report", method = RequestMethod.GET)
    public String generateChecklistReport(@RequestParam("boardId") String boardId) {
        return trelloBoardReportService.exportChecklistReport(boardId);
    }

    @RequestMapping(value = "/generate-actions-report", method = RequestMethod.GET)
    public String generateActionsReport(@RequestParam("boardId") String boardId) {
        return trelloBoardReportService.exportBoardActionsReport(boardId);
    }

    @RequestMapping(value = "/generate-labels-report", method = RequestMethod.GET)
    public String generateLabelsReport(@RequestParam("boardId") String boardId) {
        return trelloBoardReportService.exportLabelUsageReport(boardId);
    }
}
