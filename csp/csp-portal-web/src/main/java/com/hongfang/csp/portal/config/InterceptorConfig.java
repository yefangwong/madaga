/*
 * Copyright 2023 yefangwong(https://github.com/yefangwong)
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

import com.dhf.hrsys.interceptor.ThymeleafLayoutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final ThymeleafLayoutInterceptor thymeleafLayoutInterceptor;

    public InterceptorConfig(ThymeleafLayoutInterceptor thymeleafLayoutInterceptor) {
        this.thymeleafLayoutInterceptor = thymeleafLayoutInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(thymeleafLayoutInterceptor)
                .addPathPatterns(
                    "/dashboard/**",
                    "/manage/**",
                    "/energy/**")
                .excludePathPatterns("/register/**", "/css/**", "/images/**",
                        "/js/**");
    }

    @Override
    public void addResourceHandlers(org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("file:src/main/resources/static/css/", "file:csp/csp-portal-web/src/main/resources/static/css/", "classpath:/static/css/")
                .setCachePeriod(0);
        registry.addResourceHandler("/js/**")
                .addResourceLocations("file:src/main/resources/static/js/", "file:csp/csp-portal-web/src/main/resources/static/js/", "classpath:/static/js/")
                .setCachePeriod(0);
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/static/images/", "file:csp/csp-portal-web/src/main/resources/static/images/", "classpath:/static/images/")
                .setCachePeriod(0);
        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("file:src/main/resources/static/fonts/", "file:csp/csp-portal-web/src/main/resources/static/fonts/", "classpath:/static/fonts/")
                .setCachePeriod(0);
        registry.addResourceHandler("/dist/**")
                .addResourceLocations("file:src/main/resources/static/dist/", "file:csp/csp-portal-web/src/main/resources/static/dist/", "classpath:/static/dist/")
                .setCachePeriod(0);
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:src/main/resources/static/", "file:csp/csp-portal-web/src/main/resources/static/", "classpath:/static/")
                .setCachePeriod(0);
    }
}
