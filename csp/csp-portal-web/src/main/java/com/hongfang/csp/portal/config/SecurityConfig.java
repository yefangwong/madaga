/*
 * Copyright 2022 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed chat in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hongfang.csp.portal.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.anonymous();
//        //http.authorizeHttpRequests().anyRequest().authenticated().and()
//        //.formLogin().loginPage("/auth/signUp").permitAll().and().logout().permitAll();
//    }
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/emp/export",
//            "/api/question");
//
//    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/emp/export", "/api/question");
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.anonymous(Customizer.withDefaults())
            //.formLogin(form -> form.loginPage("/login").permitAll())
            //.httpBasic(Customizer.withDefaults())
            //.csrf(crsf -> crsf.disable())
        // giving permission to every request for /login endpoint
            .authorizeHttpRequests(auth -> {
                //auth.requestMatchers("/auth/signUp").permitAll()
                auth.requestMatchers("/**").permitAll();
            // for everything else, the user has to be authenticated
            // setting stateless session, because we choose to implement Rest API
            //.and().sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        });
        // adding the custom filter before UsernamePasswordAuthenticationFilter in the filter chain
        //http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
