package com.dhf.hrsys.controller;

import com.dhf.hrsys.to.Question;
import com.dhf.hrsys.to.QuestionResponse;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

/**
 * ChatGPT Server 控制端元件
 * @author yfwong
 * @date 2023/07/02
 */
@Controller
public class QuestionController {

    @PostMapping("/api/question")
    public Flux<ServerSentEvent<QuestionResponse>> sendQuestion(@RequestBody Question question) {
        QuestionResponse response = new QuestionResponse("這是答案", true);

        return Flux.just(ServerSentEvent.<QuestionResponse>builder().data(response).build());
    }
}
