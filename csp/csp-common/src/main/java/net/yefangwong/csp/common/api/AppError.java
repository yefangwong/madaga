/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.api;

import java.io.Serializable;
import java.util.Objects;

/**
 * 原創 AppError - 全域通用錯誤載體
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 核心診斷載體
 * 程 式 代 號 ：AppError.java
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：提供單筆錯誤之代碼(code)、描述(message)、欄位(field)與類別(category)封裝
 *
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public class AppError implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 預設驗證類別標籤
     */
    public static final String CATEGORY_VALIDATION = "VALIDATION";

    /**
     * 預設業務邏輯類別標籤
     */
    public static final String CATEGORY_BUSINESS = "BUSINESS";

    /**
     * 預設系統層級類別標籤
     */
    public static final String CATEGORY_SYSTEM = "SYSTEM";

    /**
     * 錯誤代碼 (e.g. "ERR_BAD_INPUT", "E4001")
     */
    private String code;

    /**
     * 錯誤人類可讀描述
     */
    private String message;

    /**
     * 相關欄位或屬性 (可選，e.g. "operatorEmail", "password")
     */
    private String field;

    /**
     * 錯誤分類標籤 (e.g. "VALIDATION", "BUSINESS", "SYSTEM")
     */
    private String category;

    public AppError() {
    }

    public AppError(String code, String message) {
        this(code, message, null, CATEGORY_BUSINESS);
    }

    public AppError(String code, String message, String field) {
        this(code, message, field, CATEGORY_BUSINESS);
    }

    public AppError(String code, String message, String field, String category) {
        this.code = code;
        this.message = message;
        this.field = field;
        this.category = (category != null) ? category : CATEGORY_BUSINESS;
    }

    // 工廠靜態方法
    public static AppError of(String code, String message) {
        return new AppError(code, message);
    }

    public static AppError of(String code, String message, String field) {
        return new AppError(code, message, field);
    }

    public static AppError of(String code, String message, String field, String category) {
        return new AppError(code, message, field, category);
    }

    public static AppError validation(String field, String message) {
        return new AppError("INVALID_INPUT", message, field, CATEGORY_VALIDATION);
    }

    public static AppErrorBuilder builder() {
        return new AppErrorBuilder();
    }

    public static class AppErrorBuilder {
        private String code;
        private String message;
        private String field;
        private String category = CATEGORY_BUSINESS;

        public AppErrorBuilder code(String code) {
            this.code = code;
            return this;
        }

        public AppErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AppErrorBuilder field(String field) {
            this.field = field;
            return this;
        }

        public AppErrorBuilder category(String category) {
            this.category = category;
            return this;
        }

        public AppError build() {
            return new AppError(code, message, field, category);
        }
    }

    // Getters & Setters
    public String getCode() {
        return code;
    }

    public AppError setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AppError setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getField() {
        return field;
    }

    public AppError setField(String field) {
        this.field = field;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public AppError setCategory(String category) {
        this.category = category;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppError appError = (AppError) o;
        return Objects.equals(code, appError.code) &&
                Objects.equals(message, appError.message) &&
                Objects.equals(field, appError.field) &&
                Objects.equals(category, appError.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, field, category);
    }

    @Override
    public String toString() {
        return "AppError{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", field='" + field + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
