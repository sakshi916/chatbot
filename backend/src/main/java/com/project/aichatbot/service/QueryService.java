package com.project.aichatbot.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Component
public class QueryService {

    @Value("${MODEL_API_KEY}")
    private String apiKey;

    private List<Map<String, String>> conversationHistory = new ArrayList<>();

    public String processQuery(String query) {
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", query);
        conversationHistory.add(userMessage);
        HttpClient client = HttpClient.newHttpClient();
        JSONObject obj = new JSONObject();
        obj.put("model","llama-3.1-8b-instant");
        obj.put("messages", conversationHistory);
        String body=obj.toString();
        HttpRequest req= HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Authorization","Bearer "+apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
        HttpResponse<String> resp=null;
        try {
           resp=client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        JSONObject res= new JSONObject(resp.body());
        JSONArray arr=(JSONArray) res.get("choices");
        JSONObject mess=(JSONObject) arr.get(0);
        JSONObject content = (JSONObject) mess.get("message");
        String response=content.get("content").toString();
        Map<String, String> assistantMessage = new HashMap<>();
        assistantMessage.put("role", "assistant");
        assistantMessage.put("content", response);
        conversationHistory.add(assistantMessage);
        return response;
    }
}
