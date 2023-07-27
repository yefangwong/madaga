package com.dhf.system.chat.listener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongfang.csp.system.service.impl.LocalCacheService;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.SneakyThrows;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class OpenAISSEEventSourceListener extends EventSourceListener
{
    private static final Logger log = LoggerFactory.getLogger(OpenAISSEEventSourceListener.class);
    private long tokens;
    private StringBuffer sql;
    private SseEmitter sseEmitter;

    public OpenAISSEEventSourceListener(SseEmitter sseEmitter) {
        this.sql = new StringBuffer();
        this.sseEmitter = sseEmitter;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("OpenAI建立sse連線...");
    }

    @SneakyThrows
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        log.info("OpenAI返回數據---：{}", data);
        tokens += 1;
        if (data.equals("[DONE]")) {
            log.info("OpenAI返回數據結束了");
            String uuid = UUID.randomUUID().toString();
            log.info("put sql:{} in uuid:{} cache.", sql(), uuid);
            LocalCacheService.putCache(uuid, sql());
            sseEmitter.send(SseEmitter.event().id("[TOKENS]").data("<br/><br/>tokens:" + tokens()).reconnectTime(3000));
            sseEmitter.send(SseEmitter.event().id("[DONE]").data("[DONE]").reconnectTime(3000));
            sseEmitter.send(SseEmitter.event().id("[UUID]").data(String.format("{\"UUID\":\"%s\"}",uuid)).reconnectTime(3000));
            // 傳輸完成後自動關閉sse
            sseEmitter.complete();
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ChatCompletionResponse completionResponse = mapper.readValue(data, ChatCompletionResponse.class); // 讀取Json
        try {
            Message delta = completionResponse.getChoices().get(0).getDelta();
            sseEmitter.send(SseEmitter.event()
                        .id(completionResponse.getId())
                        .data(delta)
                        .reconnectTime(3000));
            if (!delta.getContent().equals("null"))
                sql.append(delta.getContent());
        } catch (Exception e) {
            log.error("sse訊息推送失敗！");
            eventSource.cancel();
            e.printStackTrace();
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("流式輸出返回值總共{}tokens", tokens() - 2);
        log.info("OpenAI關閉sse連線...");
        LocalCacheService.clear();
        log.info("清除後端快取...");
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if (Objects.isNull(response)) {
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI sse連線異常data：{}，異常：{}", body.string(), t);
        } else {
            log.error("OpenAI sse連線異常data：{}，異常：{}", response, t);
        }
        eventSource.cancel();
    }

    /**
     * tokens
     * @return
     */
    public long tokens() {
        return tokens;
    }

    /**
     * sql
     * @return sql
     */
    public String sql() { return sql.toString(); }
}
