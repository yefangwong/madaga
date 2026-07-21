/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package common.api;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 原創 ApiResult - 通用 REST API 響應結果封裝
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 核心 API 響應
 * 程 式 代 號 ：ApiResult.java
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：提供強型別 Payload 泛型 <T>、Lombok Builder 模式與標準 JSON 狀態結構
 *
 * @param <T> 響應數據之強型別泛型
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2015-2026)
 */
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 回應時間
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 保留原本 csp-webframeworx-common 的 time 和 格式
    private Date time;

    private int code;
    private boolean success;
    private String message;
    private T data;

    public ApiResult(){
        time = new Date();
    }

    public ApiResult(int code, boolean success, String message, T data, Date time) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
        this.time = time;
    }

    // 工廠靜態方法
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(200, true, "Success", null, new Date());
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, true, "Success", data, new Date());
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<>(200, true, message, data, new Date());
    }

    public static <T> ApiResult<T> failure(int code, String message) {
        return new ApiResult<>(code, false, message, null, new Date());
    }

    // 原生 Builder 模式支援：ApiResult.<String>builder().code(200).build();
    public static <T> ApiResultBuilder<T> builder() {
        return new ApiResultBuilder<>();
    }


    public static class ApiResultBuilder<T> {
        protected int code = 200;
        protected boolean success = true;
        protected String message = "Success";
        protected T data;
        protected Date time;

        public ApiResultBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        public ApiResultBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public ApiResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResultBuilder<T> time(Date time) {
            this.time = time;
            return this;
        }

        public ApiResult<T> build() {
            return new ApiResult<>(code, success, message, data, time);
        }
    }

    // Getter & Setter
    public int getCode() { return code; }
    public ApiResult<T> setCode(int code) {
        this.code = code;
        return this;
    }
    public boolean isSuccess() { return success; }
    public ApiResult<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }
    public String getMessage() { return message; }
    public ApiResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }
    public T getData() { return data; }
    public ApiResult<T> setData(T data) {
        this.data = data;
        return this;
    }
    public Date getTime() { return this.time; }
    public ApiResult<T> setTime(Date time) {
        this.time = time;
        return this;
    }
}
