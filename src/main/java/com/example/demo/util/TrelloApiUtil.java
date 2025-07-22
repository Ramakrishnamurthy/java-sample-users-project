package com.example.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Component
public class TrelloApiUtil {


    private static final String BASE_URL = "https://api.trello.com/1";
    @Autowired
    private TrelloConfig trelloConfig;

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> getJson(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(trelloConfig.getBaseUrl() + path + buildAuthQuery()))
                .GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Trello API Error [" + response.statusCode() + "]: " + response.body());
        }

        return mapper.readValue(response.body(), new TypeReference<>() {});
    }

    public List<Map<String, Object>> getJsonList(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(trelloConfig.getBaseUrl() + path + buildAuthQuery()))
                .GET().build();

        System.out.println("🔐 API URL: " + trelloConfig.getBaseUrl() + path + buildAuthQuery());
        System.out.println("📦 KEY: " + trelloConfig.getKey());
        System.out.println("📦 TOKEN: " + trelloConfig.getToken());

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Trello API Error [" + response.statusCode() + "]: " + response.body());
        }

        return mapper.readValue(response.body(), new TypeReference<>() {});
    }


    private String buildAuthQuery() {
        return "?key=" + trelloConfig.getKey() + "&token=" + trelloConfig.getToken();
    }
}
