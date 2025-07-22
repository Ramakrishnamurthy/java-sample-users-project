package com.example.demo.service;

import com.example.demo.util.*;
import org.springframework.stereotype.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Service
public class TrelloReportService {

    private final TrelloApiUtil apiUtil;

    public TrelloReportService(TrelloApiUtil apiUtil) {
        this.apiUtil = apiUtil;
    }

    public String exportBoardAndLists(String boardId) {
        String fileName = "trello_board_list_data.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // CSV Header
            writer.println("BoardId,Board Name,Board URL,Board Description,ListId,List Name,List Position");

            // Get Board Info
            Map<String, Object> board = apiUtil.getJson("/boards/" + boardId);
            String boardName = sanitize((String) board.get("name"));
            String boardDesc = sanitize((String) board.get("desc"));
            String boardUrl = "https://trello.com/b/" + board.get("shortLink");

            // Get Lists on the Board
            List<Map<String, Object>> lists = apiUtil.getJsonList("/boards/" + boardId + "/lists");

            for (Map<String, Object> list : lists) {
                String listId = (String) list.get("id");
                String listName = sanitize((String) list.get("name"));
                int listPos = ((Number) list.get("pos")).intValue();

                writer.printf("%s,%s,%s,%s,%s,%s,%d%n",
                        boardId, boardName, boardUrl, boardDesc,
                        listId, listName, listPos);
            }

            return "Board and list data exported to: " + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting data: " + e.getMessage();
        }
    }

    // Optional: Simple sanitizer to avoid CSV breaking characters
    private String sanitize(String value) {
        if (value == null) return "";
        return value.replace(",", " ").replaceAll("\\s+", " ").trim();
    }
}
