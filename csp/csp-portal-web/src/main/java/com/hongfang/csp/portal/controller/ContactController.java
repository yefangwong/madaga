/*
 * Copyright 2026 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hongfang.csp.portal.controller;

import com.hongfang.csp.webframeworx.common.api.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ContactController
 * Handles customer contact inquiries and sends actual emails if SMTP is configured.
 */
@RestController
@RequestMapping("/contact")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${contact.to-email:yefang.wong@gmail.com}")
    private String toEmail;

    @PostMapping("/submit")
    public ApiResult<String> submitContact(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            @RequestParam(value = "lang", required = false, defaultValue = "zh") String lang) {

        boolean isEn = "en".equalsIgnoreCase(lang);

        logger.info("================ [收到聯絡信件] ================");
        logger.info("姓名: {}", name);
        logger.info("Email: {}", email);
        logger.info("諮詢主題: {}", subject);
        logger.info("詳細需求: {}", message);
        logger.info("語系: {}", lang);
        logger.info("=============================================");

        if (mailSender == null || fromEmail == null || fromEmail.isEmpty()) {
            logger.warn("郵件發送器未配置，將以本地日誌模式歸檔。請在 application.properties 配置 spring.mail 屬性。");
            String logMsg = isEn ? "Inquiry archived in local logs! (SMTP not configured)" : "信件已於本地日誌歸檔！(SMTP 未配置)";
            return ApiResult.ok(logMsg);
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("[DHF 官網諮詢] - " + subject);
            
            StringBuilder body = new StringBuilder();
            body.append("DHF 企業級 AI 解決方案套件 - 官網客戶諮詢通知\n");
            body.append("--------------------------------------------------\n");
            body.append("客戶姓名: ").append(name).append("\n");
            body.append("聯絡信箱: ").append(email).append("\n");
            body.append("諮詢類別: ").append(subject).append("\n");
            body.append("諮詢內容:\n").append(message).append("\n");
            body.append("--------------------------------------------------\n");
            
            mailMessage.setText(body.toString());
            
            mailSender.send(mailMessage);
            logger.info("信件成功發送至：{}", toEmail);
            String successMsg = isEn ? "Inquiry sent successfully!" : "信件已成功發送！";
            return ApiResult.ok(successMsg);
        } catch (Exception e) {
            logger.error("郵件發送失敗: ", e);
            // Return failure info matching system format
            String failMsg = isEn 
                    ? "Failed to send email. Please try again later. Reason: " + e.getMessage()
                    : "郵件發送失敗，請稍後再試。原因: " + e.getMessage();
            return ApiResult.result(null, failMsg, null);
        }
    }
}
