package com.example.demo.trello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

// ----------- Model Class -----------
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrelloBoard {
    public String id;
    public String name;
    public String desc;
    public boolean closed;
    public String url;
    public String shortUrl;
    public Prefs prefs;
    public Map<String, String> labelNames;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Prefs {
        public String permissionLevel;
        public boolean hideVotes;
        public String voting;
        public String comments;
        public String invitations;
        public boolean selfJoin;
        public boolean cardCovers;
        public boolean showCompleteStatus;
        public boolean cardCounts;
        public boolean isTemplate;
        public String cardAging;
        public boolean calendarFeedEnabled;
        public List<SwitcherView> switcherViews;
        public String background;
        public String backgroundColor;
        public String backgroundBrightness;

        @JsonProperty("backgroundBottomColor")
        public String backgroundBottomColor;

        @JsonProperty("backgroundTopColor")
        public String backgroundTopColor;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SwitcherView {
        public String viewType;
        public boolean enabled;
    }
}