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

    @PostMapping(value = "/question")
    @ResponseBody
    public ChatResponse sendQuestion(@RequestBody Question question) throws Exception {
        log.debug("execute sendQuestion."+question.getText());
        return sseService.sseChat(question);
    }
}
