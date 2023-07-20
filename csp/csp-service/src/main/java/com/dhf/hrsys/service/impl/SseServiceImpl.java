package com.dhf.hrsys.service.impl;

import cn.hutool.json.JSONUtil;
import com.dhf.hrsys.service.SseService;
import com.dhf.system.chat.ChatResponse;
import com.dhf.system.chat.Question;
import com.dhf.system.chat.listener.OpenAISSEEventSourceListener;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ChatGPT SSE 服務實作
 * @author yfwong
 * @date 2023/07/05
 */
@Service
public class SseServiceImpl implements SseService {
    private static final Logger log = LoggerFactory.getLogger(SseServiceImpl.class);

    private final OpenAiStreamClient openAiStreamClient;

    public SseServiceImpl(OpenAiStreamClient openAiStreamClient) {
        this.openAiStreamClient = openAiStreamClient;
    }
    private HashMap localCache = new HashMap();

    @Override public SseEmitter createSse() {
        //預設逾時為30秒,設定為0L表示不超時
        SseEmitter sseEmitter = new SseEmitter(0l);
        //完成后回调
        sseEmitter.onCompletion(() -> {
            log.info("结束連結...................");
        });
        //逾時回呼
        sseEmitter.onTimeout(() -> {
            log.info("連結逾時...................");
        });
        //異常回呼
        sseEmitter.onError(
            throwable -> {
                try {
                    log.info("連線異常,{}", throwable.toString());
                    sseEmitter.send(SseEmitter.event()
                        //.id(uid)
                        .name("發生異常！")
                        .data(Message.builder().content("發生異常請重試！").build())
                        .reconnectTime(3000));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );
        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(5000));
        } catch (IOException e) {
            e.printStackTrace();
        }
        localCache.put("1", sseEmitter);
        log.info("[{}]建立sse连接成功！");
        return sseEmitter;    }

    @Override public void closeSse() {
        SseEmitter sse = (SseEmitter) localCache.get("1");
        if (sse != null) {
            sse.complete();
            //移除
            localCache.remove("1");
        }
    }

    @Override public ChatResponse sseChat(Question chatRequest) throws Exception {
        if (StringUtils.isBlank(chatRequest.getText())) {
            log.info("参數異常，text為null");
            throw new Exception("参數異常，text不能為空~");
        }
        /*String prompt = Synthesizer.getInstance().synthesize(chatRequest.getText());
        log.info("prompt:{}", prompt);
        chatRequest.setText(prompt);*/
        chatRequest.setText(chatRequest.getText());
        String messageContext = (String) localCache.get("msg1");
        List<Message> messages = new ArrayList<>();
        if (StringUtils.isNotBlank(messageContext)) {
            messages = JSONUtil.toList(messageContext, Message.class);
            if (messages.size() >= 10) {
                messages = messages.subList(1, 10);
            }
            Message currentMessage = Message.builder().content(chatRequest.getText()).role(Message.Role.USER).build();
            messages.add(currentMessage);
        } else {
            Message currentMessage =
                    Message.builder().content(chatRequest.getText()).role(Message.Role.USER).build();
            messages.add(currentMessage);
        }

        SseEmitter sseEmitter = (SseEmitter) localCache.get("1");

        if (sseEmitter == null) {
            log.info("聊天訊息推送失敗,没有建立連接，請重試。");
            throw new Exception("聊天訊息推送失敗,没有建立連接，請重試。~");
        }
        OpenAISSEEventSourceListener openAIEventSourceListener = new OpenAISSEEventSourceListener(sseEmitter);
        ChatCompletion completion = ChatCompletion
            .builder()
            .messages(messages)
            .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
            .build();
        openAiStreamClient.streamChatCompletion(completion, openAIEventSourceListener);
        localCache.put("msg1", JSONUtil.toJsonStr(messages));
        ChatResponse response = new ChatResponse();
        response.setQuestionTokens(completion.tokens());
        return response;
    }
}
