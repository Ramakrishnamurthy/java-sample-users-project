package com.example.demo.controller;

import com.example.demo.service.*;
import com.example.demo.trello.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// ----------- REST Controller -----------
@RestController
@RequestMapping("/api/trello")
class TrelloController {

    private final TrelloService trelloService;

    public TrelloController(TrelloService trelloService) {
        this.trelloService = trelloService;
    }

    @GetMapping("/board")
    public ResponseEntity<TrelloBoard> getBoard() {
        try {
            TrelloBoard board = trelloService.getBoardDetails();
            return ResponseEntity.ok(board);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
