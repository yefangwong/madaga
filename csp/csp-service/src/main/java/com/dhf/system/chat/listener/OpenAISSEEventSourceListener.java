package com.dhf.system.chat.listener;
import okhttp3.sse.EventSourceListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class OpenAISSEEventSourceListener extends EventSourceListener
{
    public OpenAISSEEventSourceListener(SseEmitter sseEmitter) {

    }
}
