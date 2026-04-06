package com.dhf.hrsys.controller;

import com.dhf.hrsys.service.SseService;
import com.dhf.system.chat.ChatResponse;
import com.dhf.system.chat.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuestionControllerTest {

    private QuestionController questionController;

    @Mock
    private SseService sseService;

    @Mock
    private Question question;

    @Mock
    private ChatResponse chatResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        questionController = new QuestionController(sseService);
    }

    @Test
    public void testStartSSE() throws Exception {
        SseEmitter expectedEmitter = new SseEmitter();
        when(sseService.createSse()).thenReturn(expectedEmitter);

        SseEmitter actualEmitter = questionController.startSSE();

        assertNotNull(actualEmitter);
        assertEquals(expectedEmitter, actualEmitter);
        verify(sseService, times(1)).createSse();
    }

    @Test
    public void testSendQuestion() throws Exception {
        when(question.getText()).thenReturn("Hello ChatGPT");
        when(sseService.sseChat(any(Question.class))).thenReturn(chatResponse);

        ChatResponse actualResponse = questionController.sendQuestion(question);

        assertNotNull(actualResponse);
        assertEquals(chatResponse, actualResponse);
        verify(sseService, times(1)).sseChat(question);
    }
}
