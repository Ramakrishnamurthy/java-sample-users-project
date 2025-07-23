package com.example.demo.service;

import com.example.demo.util.TrelloApiUtil;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
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
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(fileName));
            writer.println("BoardId,Board Name,Board URL,Board Description,ListId,List Name,List Position");

            Map board = apiUtil.getJson("/boards/" + boardId);
            String boardName = sanitize((String) board.get("name"));
            String boardDesc = sanitize((String) board.get("desc"));
            String boardUrl = "https://trello.com/b/" + board.get("shortLink");

            List lists = apiUtil.getJsonList("/boards/" + boardId + "/lists");

            for (Object obj : lists) {
                Map list = (Map) obj;
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
        } finally {
            if (writer != null) writer.close();
        }
    }

    public String exportCardsOverview(String boardId) {
        String fileName = "cards_overview.csv";
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(fileName));
            writer.println("CardId,Card Name,Card URL,Card Description,ListId,List Name,Last Activity,Due Date,Labels");

            List cards = apiUtil.getJsonList("/boards/" + boardId + "/cards");

            for (Object cardObj : cards) {
                Map card = (Map) cardObj;
                String cardId = (String) card.get("id");
                String name = sanitize((String) card.get("name"));
                String url = (String) card.get("shortUrl");
                String desc = sanitize((String) card.get("desc"));
                String listId = (String) card.get("idList");
                String lastActivity = (String) card.get("dateLastActivity");
                String due = card.get("due") != null ? card.get("due").toString() : "";

                List labels = (List) card.get("labels");
                StringBuilder labelNames = new StringBuilder();
                for (Object labelObj : labels) {
                    Map label = (Map) labelObj;
                    labelNames.append(label.get("name")).append("; ");
                }

                Map list = apiUtil.getJson("/lists/" + listId);
                String listName = sanitize((String) list.get("name"));

                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        cardId, name, url, desc, listId, listName, lastActivity, due, labelNames.toString().trim());
            }

            return "Card overview exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting card overview: " + e.getMessage();
        } finally {
            if (writer != null) writer.close();
        }
    }

    public String exportMembersParticipation(String boardId) {
        String fileName = "members_participation.csv";
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(fileName));
            writer.println("MemberId,Full Name,Username,Cards Assigned,Avatar,Role,Initials");

            List members = apiUtil.getJsonList("/boards/" + boardId + "/members");
            List cards = apiUtil.getJsonList("/boards/" + boardId + "/cards");

            for (Object memberObj : members) {
                Map member = (Map) memberObj;
                String id = (String) member.get("id");
                String fullName = sanitize((String) member.get("fullName"));
                String username = sanitize((String) member.get("username"));
                String avatar = (String) member.get("avatarUrl");
                String initials = (String) member.get("initials");

                int assignedCount = 0;
                for (Object cardObj : cards) {
                    Map card = (Map) cardObj;
                    List ids = (List) card.get("idMembers");
                    if (ids != null && ids.contains(id)) {
                        assignedCount++;
                    }
                }

                writer.printf("%s,%s,%s,%d,%s,%s,%s%n",
                        id, fullName, username, assignedCount, avatar, "member", initials);
            }

            return "Members report exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting members report: " + e.getMessage();
        } finally {
            if (writer != null) writer.close();
        }
    }

    public String exportChecklistReport(String boardId) {
        String fileName = "card_checklist_status.csv";
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(fileName));
            writer.println("CardId,Card Name,ChecklistId,Checklist Name,Total Items,Completed Items,Percent Complete");

            List cards = apiUtil.getJsonList("/boards/" + boardId + "/cards");

            for (Object cardObj : cards) {
                Map card = (Map) cardObj;
                String cardId = (String) card.get("id");
                String cardName = sanitize((String) card.get("name"));
                List checklists = apiUtil.getJsonList("/cards/" + cardId + "/checklists");

                for (Object checklistObj : checklists) {
                    Map checklist = (Map) checklistObj;
                    String checklistId = (String) checklist.get("id");
                    String checklistName = sanitize((String) checklist.get("name"));
                    List items = (List) checklist.get("checkItems");
                    int total = items.size();
                    int done = 0;

                    for (Object itemObj : items) {
                        Map item = (Map) itemObj;
                        if ("complete".equals(item.get("state"))) {
                            done++;
                        }
                    }

                    int percent = total > 0 ? (done * 100 / total) : 0;

                    writer.printf("%s,%s,%s,%s,%d,%d,%d%%%n",
                            cardId, cardName, checklistId, checklistName, total, done, percent);
                }
            }

            return "Checklist report exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting checklist report: " + e.getMessage();
        } finally {
            if (writer != null) writer.close();
        }
    }

    public String exportBoardActionsReport(String boardId) {
        String fileName = "board_actions_report.csv";
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(fileName));
            writer.println("ActionId,Type,Date,Card Name,Member,Old Value,New Value");

            List actions = apiUtil.getJsonList("/boards/" + boardId + "/actions?limit=50");

            for (Object actionObj : actions) {
                Map action = (Map) actionObj;
                String actionId = (String) action.get("id");
                String type = (String) action.get("type");
                String date = (String) action.get("date");
                Map data = (Map) action.get("data");

                String cardName = "";
                if (data.get("card") != null) {
                    Map card = (Map) data.get("card");
                    cardName = sanitize((String) card.get("name"));
                }

                Map member = (Map) action.get("memberCreator");
                String memberName = (String) member.get("fullName");

                String oldValue = data.containsKey("old") ? data.get("old").toString() : "";
                String newValue = data.containsKey("card") ? data.get("card").toString() : "";

                writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                        actionId, type, date, cardName, memberName, oldValue, newValue);
            }

            return "Board actions report exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting actions report: " + e.getMessage();
        } finally {
            if (writer != null) writer.close();
        }
    }

    public String exportLabelUsageReport(String boardId) {
        String fileName = "label_usage_report.csv";
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(fileName));
            writer.println("LabelId,Label Name,Color,Used On Cards");

            List labels = apiUtil.getJsonList("/boards/" + boardId + "/labels");
            List cards = apiUtil.getJsonList("/boards/" + boardId + "/cards");

            for (Object labelObj : labels) {
                Map label = (Map) labelObj;
                String labelId = (String) label.get("id");
                String name = sanitize((String) label.get("name"));
                String color = (String) label.get("color");

                int usageCount = 0;
                for (Object cardObj : cards) {
                    Map card = (Map) cardObj;
                    List cardLabels = (List) card.get("labels");

                    if (cardLabels != null) {
                        for (Object l : cardLabels) {
                            Map lbl = (Map) l;
                            if (labelId.equals(lbl.get("id"))) {
                                usageCount++;
                                break;
                            }
                        }
                    }
                }

                writer.printf("%s,%s,%s,%d%n", labelId, name, color, usageCount);
            }

            return "Label usage report exported to: " + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error exporting label usage: " + e.getMessage();
        } finally {
            if (writer != null) writer.close();
        }
    }

    private String sanitize(String value) {
        if (value == null) return "";
        return value.replace(",", " ").replaceAll("\\s+", " ").trim();
    }
}
