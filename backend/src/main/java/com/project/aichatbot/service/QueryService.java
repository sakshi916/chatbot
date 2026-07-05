package com.project.aichatbot.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


    public SseEmitter streamChat(String message) {
        SseEmitter emitter = new SseEmitter();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                JSONObject obj = new JSONObject();
                obj.put("model", "llama-3.1-8b-instant");
                obj.put("stream", true); //to enable streaming through groq
                JSONArray messages = new JSONArray();
                JSONObject msg = new JSONObject();
                msg.put("role", "user");
                msg.put("content", message);
                messages.put(msg);
                obj.put("messages", messages);

                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                        .header("Authorization", "Bearer " + apiKey)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(obj.toString()))
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                // Stream body line by line
                client.send(req, HttpResponse.BodyHandlers.ofLines())
                        .body()
                        .forEach(line -> {
                            try {
                                if (line.startsWith("data: ") && !line.contains("[DONE]")) {
                                    String json = line.substring(6); // strip "data: "
                                    JSONObject chunk = new JSONObject(json);
                                    String token = chunk
                                            .getJSONArray("choices")
                                            .getJSONObject(0)
                                            .getJSONObject("delta")
                                            .optString("content", "");
                                    if (!token.isEmpty()) {
                                        emitter.send(token); // push token to frontend
                                    }
                                }
                            } catch (Exception e) {
                                // skip malformed chunks
                            }
                        });
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}
