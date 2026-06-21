/*
 * Copyright 2022 yefangwong(https://github.com/yefangwong)
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

package com.hongfang.csp.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/emp/export",
                "/api/question");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/contact/submit"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/index_zh", "/sustainability", "/sustainability_zh", "/suite", "/suite_zh",
                                "/auth/signUp", "/auth/signIn", "/css/**", "/js/**", "/images/**", "/energy/**", "/favicon.ico", "/contact/submit")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/auth/signUp")
                        .loginProcessingUrl("/auth/signIn")
                        .defaultSuccessUrl("/dashboard/index", true)
                        .failureUrl("/auth/signUp?error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/auth/signOut")
                        .logoutSuccessUrl("/?logout")
                        .permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler()));
        return http.build();
    }

    @Bean
    public org.springframework.security.web.access.AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            String accept = request.getHeader("Accept");
            String requestedWith = request.getHeader("X-Requested-With");
            String uri = request.getRequestURI();

            // 判斷是否為 API/JSON 請求，或者是 AJAX 請求
            if ((accept != null && accept.contains("application/json"))
                    || "XMLHttpRequest".equals(requestedWith)
                    || uri.startsWith("/sysUser/info/")) {

                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(
                        "{\"code\":403,\"success\":false,\"message\":\"您沒有存取此資源的權限 / Access Denied\",\"data\":null}");
            } else {
                // 回傳精緻的人性化 HTML 錯誤頁面
                response.setContentType("text/html;charset=UTF-8");
                response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(
                        "<!DOCTYPE html>" +
                                "<html>" +
                                "<head>" +
                                "    <meta charset=\"UTF-8\">" +
                                "    <title>403 Access Denied</title>" +
                                "    <style>" +
                                "        body { background: #f8f9fa; font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, sans-serif; display: flex; align-items: center; justify-content: center; height: 100vh; margin: 0; color: #333; }"
                                +
                                "        .card { background: white; padding: 40px; border-radius: 12px; box-shadow: 0 4px 30px rgba(0,0,0,0.05); text-align: center; max-width: 450px; border: 1px solid #e9ecef; }"
                                +
                                "        h1 { color: #dc3545; font-size: 72px; margin: 0 0 10px 0; font-weight: 700; }"
                                +
                                "        h2 { font-size: 20px; margin: 0 0 20px 0; font-weight: 600; color: #495057; }"
                                +
                                "        p { color: #6c757d; font-size: 14px; line-height: 1.6; margin: 0 0 30px 0; }" +
                                "        .btn { display: inline-block; background: #198754; color: white; text-decoration: none; padding: 10px 24px; border-radius: 6px; font-weight: 500; font-size: 14px; transition: all 0.3s; }"
                                +
                                "        .btn:hover { background: #157347; transform: translateY(-1px); }" +
                                "    </style>" +
                                "</head>" +
                                "<body>" +
                                "    <div class=\"card\">" +
                                "        <h1>403</h1>" +
                                "        <h2>存取被拒絕 / Access Denied</h2>" +
                                "        <p>抱歉，您目前的帳號沒有足夠的權限存取此資源。如果您認為這是個錯誤，請聯絡系統管理員。</p>" +
                                "        <a href=\"/dashboard/index\" class=\"btn\">返回首頁</a>" +
                                "    </div>" +
                                "</body>" +
                                "</html>");
            }
        };
    }

}
