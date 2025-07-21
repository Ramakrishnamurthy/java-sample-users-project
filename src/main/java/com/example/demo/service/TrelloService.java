package com.example.demo.service;

import com.example.demo.trello.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

// ----------- Service Class -----------
@Service
public class TrelloService {

    @Value("${trello.api.key}")
    private String apiKey;

    @Value("${trello.api.token}")
    private String apiToken;

    private final String BOARD_ID = "6878bcdb85dc33d9d7fc9aa8";

    private final RestTemplate restTemplate = new RestTemplate();

    public TrelloBoard getBoardDetails() throws Exception {
        String url = String.format("https://api.trello.com/1/boards/%s?key=%s&token=%s", BOARD_ID, apiKey, apiToken);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.getBody(), TrelloBoard.class);
    }
}