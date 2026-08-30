package com.dhf.hrsys.controller;

import com.dhf.hrsys.service.SseService;
import com.dhf.system.chat.ChatResponse;
import com.dhf.system.chat.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * ChatGPT Server 控制端元件
 * @author yfwong
 * @date 2023/07/02
 */
@Controller
@RequestMapping("/api")
@Slf4j
public class QuestionController {
    private final SseService sseService;

    public QuestionController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(value = "/startSSE")
    public SseEmitter startSSE() throws IOException {
        SseEmitter emitter = sseService.createSse();
        return emitter;
    }

    @GetMapping(value = "/assistant/status")
    @ResponseBody
    public java.util.Map<String, Object> getAssistantStatus() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        String nlpHost = System.getProperty("nlp.host", System.getenv().getOrDefault("NLP_HOST", "http://localhost"));
        int nlpPort = 9000;
        try {
            String portStr = System.getProperty("nlp.port", System.getenv().getOrDefault("NLP_PORT", "9000"));
            nlpPort = Integer.parseInt(portStr);
        } catch (NumberFormatException ignored) {}

        boolean nlpOnline = false;
        try {
            String cleanHost = nlpHost.replace("http://", "").replace("https://", "");
            if (cleanHost.contains(":")) {
                cleanHost = cleanHost.substring(0, cleanHost.indexOf(":"));
            }
            if (cleanHost.contains("/")) {
                cleanHost = cleanHost.substring(0, cleanHost.indexOf("/"));
            }
            try (java.net.Socket socket = new java.net.Socket()) {
                socket.connect(new java.net.InetSocketAddress(cleanHost, nlpPort), 600);
                nlpOnline = true;
            }
        } catch (Exception e) {
            nlpOnline = false;
        }

        result.put("online", nlpOnline);
        result.put("service", "Stanford CoreNLP");
        result.put("host", nlpHost);
        result.put("port", nlpPort);
        result.put("message", nlpOnline ? "在線上 · 即時為您解答" : "離線中 · Stanford NLP 服務未啟動");
        return result;
    }

    @PostMapping(value = "/question")
    @ResponseBody
    public ChatResponse sendQuestion(@RequestBody Question question) throws Exception {
        log.debug("execute sendQuestion."+question.getText());
        return sseService.sseChat(question);
    }
}
