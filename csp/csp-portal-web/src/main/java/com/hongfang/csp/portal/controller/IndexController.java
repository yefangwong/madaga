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

package com.hongfang.csp.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping(value = {"/", "/index", "/index_zh"})
    public String homePageZh() {
        return "index_zh";
    }

    @RequestMapping(value = {"/index_en"})
    public String homePageEn() {
        return "index";
    }

    @RequestMapping(value = {"/sustainability"})
    public String sustainability(Model model) {
        model.addAttribute("title", "Sustainability");
        model.addAttribute("description", "We are building a comprehensive framework to measure, analyze, and optimize digital carbon footprints and sustainable software development practices.");
        model.addAttribute("switchUrl", "/sustainability_zh");
        return "coming_soon";
    }

    @RequestMapping(value = {"/sustainability_zh"})
    public String sustainabilityZh(Model model) {
        model.addAttribute("title", "永續數位建設 (Sustainability)");
        model.addAttribute("description", "我們正在建置一套完善的架構，用以衡量、分析並優化數位碳足跡，以及綠色低碳軟體的開發實踐。");
        model.addAttribute("switchUrl", "/sustainability");
        return "coming_soon_zh";
    }

    @RequestMapping(value = {"/suite"})
    public String suite(Model model) {
        model.addAttribute("title", "Enterprise AI Solution Suite");
        model.addAttribute("description", "An advanced suite combining MADAGA Text-to-SQL engine (optimized with local TF-IDF Schema Linking for sub-millisecond mapping) and LLM-SQL-Guard security layer to enable secure, natural language data querying.");
        model.addAttribute("switchUrl", "/suite_zh");
        return "coming_soon";
    }

    @RequestMapping(value = {"/suite_zh"})
    public String suiteZh(Model model) {
        model.addAttribute("title", "企業級 AI 解決方案套件 (AI Solution Suite)");
        model.addAttribute("description", "結合 MADAGA 自然語言轉 SQL 引擎（導入本地 TF-IDF Schema Linking 達毫秒級架構匹配）與 LLM-SQL-Guard 安全防禦層的先進套件，實現安全、高效且人性化的數據查詢。");
        model.addAttribute("switchUrl", "/suite");
        return "coming_soon_zh";
    }
}
