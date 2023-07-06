package com.dhf.hrsys.controller;

import com.dhf.hrsys.service.SseService;
import com.dhf.system.chat.ChatResponse;
import com.dhf.system.chat.Question;
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
public class QuestionController {
    private final SseService sseService;

    public QuestionController(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(value = "/startSSE")
    public SseEmitter startSSE() throws IOException {
        System.out.println("startSSE execute.");
        //SseEmitter emitter = new SseEmitter();
        // 发送消息
        //emitter.send("Hello, world!");
        SseEmitter emitter = sseService.createSse();
        return emitter;
    }

    @PostMapping(value = "/question")
    @ResponseBody
    public ChatResponse sendQuestion(@RequestBody Question question) throws Exception {
        System.out.println("execute sendQuestion."+question.getText());
        return sseService.sseChat(question);
    }
}
