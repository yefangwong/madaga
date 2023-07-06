package com.dhf.hrsys.controller;

import com.dhf.system.chat.Question;
import com.dhf.system.chat.QuestionResponse;
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
    public QuestionController() {
    }

    @GetMapping(value = "/startSSE")
    public SseEmitter startSSE() throws IOException {
        System.out.println("startSSE execute.");
        SseEmitter emitter = new SseEmitter();
        // 发送消息
        emitter.send("Hello, world!");
        return emitter;
    }

    @PostMapping(value = "/question")
    @ResponseBody
    public QuestionResponse sendQuestion(@RequestBody Question question) {
        System.out.println("execute sendQuestion."+question.getText());

        QuestionResponse response = new QuestionResponse("這是答案", true);
        return response;
    }
}
