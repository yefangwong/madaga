package com.hongfang.csp.portal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class Nl2SqlController {

    // 透過環境變數注入金鑰 (MADAGA-UI-001: 金鑰僅存在後端環境變數)
    @Value("${csp.ai.openai.api-key:}")
    private String openAiApiKey;

    @PostMapping("/nl2sql")
    public ResponseEntity<Map<String, Object>> generateSql(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        log.info("Receiving NL2SQL request, prompt length: {}", prompt != null ? prompt.length() : 0);

        if (prompt == null || prompt.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Prompt cannot be empty"));
        }

        if (openAiApiKey == null || openAiApiKey.trim().isEmpty()) {
            log.error("OpenAI API Key is not configured in backend environment.");
            return ResponseEntity.internalServerError().body(Map.of("error", "AI provider is not properly configured."));
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("model", "gpt-3.5-turbo-instruct"); // updated model as text-davinci-002 is deprecated
            reqBody.put("prompt", "Create a SQL request to " + prompt);
            reqBody.put("temperature", 0.3);
            reqBody.put("max_tokens", 60);
            reqBody.put("top_p", 1.0);
            reqBody.put("frequency_penalty", 0.0);
            reqBody.put("presence_penalty", 0.0);
            reqBody.put("stop", List.of("#", ";"));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(reqBody, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.openai.com/v1/completions",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    String text = (String) choices.get(0).get("text");
                    return ResponseEntity.ok(Map.of("result", text != null ? text.trim() : ""));
                }
            }
            return ResponseEntity.ok(Map.of("result", ""));
        } catch (Exception e) {
            log.error("Error calling OpenAI: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to generate SQL."));
        }
    }
}
