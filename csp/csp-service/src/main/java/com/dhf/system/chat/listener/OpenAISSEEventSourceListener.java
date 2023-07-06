package com.dhf.system.chat.listener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;
public class OpenAISSEEventSourceListener extends EventSourceListener
{
    private static final Logger log = LoggerFactory.getLogger(OpenAISSEEventSourceListener.class);

    private long tokens;
    private SseEmitter sseEmitter;
    public OpenAISSEEventSourceListener(SseEmitter sseEmitter) {
        this.sseEmitter = sseEmitter;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("OpenAI建立sse連線...");
    }

    @SneakyThrows
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        log.info("OpenAI返回數據：{}", data);
        tokens += 1;
        if (data.equals("[DONE]")) {
            log.info("OpenAI返回數據結束了");
            sseEmitter.send(SseEmitter.event()
                    .id("[TOKENS]")
                    .data("<br/><br/>tokens:" + tokens())
                    .reconnectTime(3000));
            sseEmitter.send(SseEmitter.event()
                    .id("[DONE]")
                    .data("[DONE]")
                    .reconnectTime(3000));
            // 傳輸完成後自動關閉sse
            sseEmitter.complete();
            ObjectMapper mapper = new ObjectMapper();
            ChatCompletionResponse completionResponse = mapper.readValue(data, ChatCompletionResponse.class); // 讀取Json
            try {
                sseEmitter.send(SseEmitter.event()
                        .id(completionResponse.getId())
                        .data(completionResponse.getChoices().get(0).getDelta())
                        .reconnectTime(3000));
            } catch (Exception e) {
                log.error("sse訊息推送失敗！");
                eventSource.cancel();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("流式輸出返回值總共{}tokens", tokens() - 2);
        log.info("OpenAI關閉sse連線...");
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

}
