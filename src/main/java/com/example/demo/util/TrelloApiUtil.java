package com.example.demo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Component
public class TrelloApiUtil {

    private static final String BASE_URL = "https://api.trello.com/1";

    @Autowired
    private TrelloConfig trelloConfig;

    private static final ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> getJson(String path) throws Exception {
        String apiUrl = trelloConfig.getBaseUrl() + path + buildAuthQuery();
        System.out.println("🔐 API URL: " + apiUrl);
        System.out.println("📦 KEY: " + trelloConfig.getKey());
        System.out.println("📦 TOKEN: " + trelloConfig.getToken());

        String response = sendHttpRequest(apiUrl);

        System.out.println("📦 Response: " + response);

        return mapper.readValue(response, new TypeReference<Map<String, Object>>() {});
    }

    public List<Map<String, Object>> getJsonList(String path) throws Exception {
        String apiUrl = trelloConfig.getBaseUrl() + path + buildAuthQuery();
        System.out.println("🔐 API URL: " + apiUrl);
        System.out.println("📦 KEY: " + trelloConfig.getKey());
        System.out.println("📦 TOKEN: " + trelloConfig.getToken());

        String response = sendHttpRequest(apiUrl);

        System.out.println("📦 Response: " + response);

        return mapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
    }

    private String sendHttpRequest(String urlStr) throws Exception {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int status = connection.getResponseCode();
            InputStream inputStream = (status == 200) ? connection.getInputStream() : connection.getErrorStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            if (status != 200) {
                throw new RuntimeException("Trello API Error [" + status + "]: " + sb.toString());
            }

            return sb.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignore) {}
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String buildAuthQuery() {
        return "?key=" + trelloConfig.getKey() + "&token=" + trelloConfig.getToken();
    }
}
