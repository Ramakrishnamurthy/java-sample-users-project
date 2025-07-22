package com.example.demo.service;

import com.example.demo.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class TrelloReportService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public String generateReport(String boardId) {
        String fileName = "trello_board_data.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            writer.println("BoardId,Board Name,ListId,List Name,Board Description,CardId,Card Name,Checklist Name");

            // Get Board Info
            Map<String, Object> board = TrelloApiUtil.getJson("/boards/" + boardId);
            String boardName = (String) board.get("name");
            String boardDesc = sanitize((String) board.get("desc"));

            // Get Lists
            List<Map<String, Object>> lists = TrelloApiUtil.getJsonList("/boards/" + boardId + "/lists");

            for (Map<String, Object> list : lists) {
                String listId = (String) list.get("id");
                String listName = sanitize((String) list.get("name"));

                // Get Cards
                List<Map<String, Object>> cards = TrelloApiUtil.getJsonList("/lists/" + listId + "/cards");

                for (Map<String, Object> card : cards) {
                    String cardId = (String) card.get("id");
                    String cardName = sanitize((String) card.get("name"));

                    // Get Checklists
                    List<Map<String, Object>> checklists = TrelloApiUtil.getJsonList("/cards/" + cardId + "/checklists");

                    if (checklists.isEmpty()) {
                        writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                                boardId, boardName, listId, listName, boardDesc, cardId, cardName, "");
                    }

                    for (Map<String, Object> checklist : checklists) {
                        String checklistName = sanitize((String) checklist.get("name"));
                        writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                                boardId, boardName, listId, listName, boardDesc, cardId, cardName, checklistName);
                    }
                }
            }

            return "Report generated: " + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating report: " + e.getMessage();
        }
    }

    private String sanitize(String value) {
        return value == null ? "" : value.replaceAll("[\\r\\n,]", " ").trim();
    }
}
