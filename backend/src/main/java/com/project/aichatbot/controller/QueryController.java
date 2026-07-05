package com.project.aichatbot.controller;

import com.project.aichatbot.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class QueryController {

    @Autowired
    QueryService service;

    @PostMapping("/query")
    public String query(@RequestBody String query){

        return service.processQuery(query);
    }
    @GetMapping("/api/chat/stream")
    public SseEmitter streamChat(@RequestParam String message) {
        // push each chunk via emitter.send(chunk)
        return service.streamChat(message);
    }
}
