package com.example.coladder.service;

import com.example.coladder.pojo.Interactive;
import com.example.coladder.pojo.Result;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface ApiService {

    void callModel(SseEmitter sseEmitter,String input) throws IOException;

    Result callmodel2(String question) throws IOException;

    Interactive[] selectByUserID(Integer id);

    Result getAnswer(String question);
}
