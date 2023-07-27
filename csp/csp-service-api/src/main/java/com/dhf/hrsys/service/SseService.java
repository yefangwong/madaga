package com.dhf.hrsys.service;

import com.dhf.system.chat.ChatResponse;
import com.dhf.system.chat.Question;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE 服務介面，連接 ChatGPT用
 * @author yfwong
 * @date 2023/07/05
 */
public interface SseService {
    /**
     * 建立SSE
     * @return
     */
    SseEmitter createSse();

    /**
     * 關閉SSE
     */
    void closeSse();

    /**
     * 客户端發送訊息到服務端
     * @param chatRequest
     */
    ChatResponse sseChat(Question chatRequest) throws Exception;
}
