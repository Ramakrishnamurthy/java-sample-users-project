package com.example.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class TrelloApiUtil {

    private static final String API_KEY = "5e727952d469aa0b96ebaf579fa34cc3";
    private static final String TOKEN = "ATTA4a4bbdceeadeeb549ae484030c9d77eb7f8a0a36e74db43e01857187dbb065da8B11A722";
    private static final String BASE_URL = "https://api.trello.com/1";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> getJson(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path + buildAuthQuery()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<>() {});
    }

    public static List<Map<String, Object>> getJsonList(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path + buildAuthQuery()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<>() {});
    }

    private static String buildAuthQuery() {
        return "?key=" + API_KEY + "&token=" + TOKEN;
    }
}
