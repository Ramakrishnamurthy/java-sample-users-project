package com.example.demo.service;

import com.example.demo.util.*;
import org.springframework.stereotype.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Service
public class TrelloBoardReportService {

    private final TrelloApiUtil apiUtil;

    public TrelloBoardReportService(TrelloApiUtil apiUtil) {
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

    public String exportCardsOverview(String boardId) {
        String fileName = "cards_overview.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("CardId,Card Name,Card URL,Card Description,ListId,List Name,Last Activity,Due Date,Labels");

            List<Map<String, Object>> cards = apiUtil.getJsonList("/boards/" + boardId + "/cards");

            for (Map<String, Object> card : cards) {
                String cardId = (String) card.get("id");
                String name = sanitize((String) card.get("name"));
                String url = (String) card.get("shortUrl");
                String desc = sanitize((String) card.get("desc"));
                String listId = (String) card.get("idList");
                String lastActivity = (String) card.get("dateLastActivity");
                String due = card.get("due") != null ? card.get("due").toString() : "";

                List<Map<String, Object>> labels = (List<Map<String, Object>>) card.get("labels");
                String labelNames = "";
                for (Map<String, Object> label : labels) {
                    labelNames += label.get("name") + "; ";
                }

                Map<String, Object> list = apiUtil.getJson("/lists/" + listId);
                String listName = sanitize((String) list.get("name"));

                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        cardId, name, url, desc, listId, listName, lastActivity, due, labelNames.trim());
            }
            return "Card overview exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting card overview: " + e.getMessage();
        }
    }

    public String exportMembersParticipation(String boardId) {
        String fileName = "members_participation.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("MemberId,Full Name,Username,Cards Assigned,Avatar,Role,Initials");

            List<Map<String, Object>> members = apiUtil.getJsonList("/boards/" + boardId + "/members");
            List<Map<String, Object>> cards = apiUtil.getJsonList("/boards/" + boardId + "/cards");

            for (Map<String, Object> member : members) {
                String id = (String) member.get("id");
                String fullName = sanitize((String) member.get("fullName"));
                String username = sanitize((String) member.get("username"));
                String avatar = (String) member.get("avatarUrl");
                String initials = (String) member.get("initials");

                long assignedCount = cards.stream()
                        .filter(card -> {
                            List<String> ids = (List<String>) card.get("idMembers");
                            return ids != null && ids.contains(id);
                        }).count();

                writer.printf("%s,%s,%s,%d,%s,%s,%s%n",
                        id, fullName, username, assignedCount, avatar, "member", initials);
            }
            return "Members report exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting members report: " + e.getMessage();
        }
    }

    public String exportChecklistReport(String boardId) {
        String fileName = "card_checklist_status.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("CardId,Card Name,ChecklistId,Checklist Name,Total Items,Completed Items,Percent Complete");

            List<Map<String, Object>> cards = apiUtil.getJsonList("/boards/" + boardId + "/cards");

            for (Map<String, Object> card : cards) {
                String cardId = (String) card.get("id");
                String cardName = sanitize((String) card.get("name"));
                List<Map<String, Object>> checklists = apiUtil.getJsonList("/cards/" + cardId + "/checklists");

                for (Map<String, Object> checklist : checklists) {
                    String checklistId = (String) checklist.get("id");
                    String checklistName = sanitize((String) checklist.get("name"));
                    List<Map<String, Object>> items = (List<Map<String, Object>>) checklist.get("checkItems");
                    int total = items.size();
                    int done = (int) items.stream().filter(i -> "complete".equals(i.get("state"))).count();
                    int percent = total > 0 ? (done * 100 / total) : 0;

                    writer.printf("%s,%s,%s,%s,%d,%d,%d%%%n",
                            cardId, cardName, checklistId, checklistName, total, done, percent);
                }
            }
            return "Checklist report exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting checklist report: " + e.getMessage();
        }
    }

    public String exportBoardActionsReport(String boardId) {
        String fileName = "board_actions_report.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("ActionId,Type,Date,Card Name,Member,Old Value,New Value");
            List<Map<String, Object>> actions = apiUtil.getJsonList("/boards/" + boardId + "/actions?limit=50");

            for (Map<String, Object> action : actions) {
                String actionId = (String) action.get("id");
                String type = (String) action.get("type");
                String date = (String) action.get("date");
                Map<String, Object> data = (Map<String, Object>) action.get("data");
                String cardName = data.get("card") != null ? sanitize((String) ((Map<String, Object>) data.get("card")).get("name")) : "";
                String member = (String) ((Map<String, Object>) action.get("memberCreator")).get("fullName");
                String oldValue = data.containsKey("old") ? data.get("old").toString() : "";
                String newValue = data.containsKey("card") ? ((Map<String, Object>) data.get("card")).toString() : "";

                writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                        actionId, type, date, cardName, member, oldValue, newValue);
            }
            return "Board actions report exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting actions report: " + e.getMessage();
        }
    }

    public String exportLabelUsageReport(String boardId) {
        String fileName = "label_usage_report.csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("LabelId,Label Name,Color,Used On Cards");

            List<Map<String, Object>> labels = apiUtil.getJsonList("/boards/" + boardId + "/labels");
            List<Map<String, Object>> cards = apiUtil.getJsonList("/boards/" + boardId + "/cards");

            for (Map<String, Object> label : labels) {
                String labelId = (String) label.get("id");
                String name = sanitize((String) label.get("name"));
                String color = (String) label.get("color");

                long usageCount = cards.stream()
                        .filter(card -> {
                            List<Map<String, Object>> cardLabels = (List<Map<String, Object>>) card.get("labels");
                            return cardLabels != null && cardLabels.stream()
                                    .anyMatch(l -> labelId.equals(l.get("id")));
                        }).count();

                writer.printf("%s,%s,%s,%d%n",
                        labelId, name, color, usageCount);
            }
            return "Label usage report exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting label usage: " + e.getMessage();
        }
    }

}
