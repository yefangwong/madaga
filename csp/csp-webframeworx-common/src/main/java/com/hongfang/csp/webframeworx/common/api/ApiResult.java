/*
 * Copyright 2020 yefangwong(https://github.com/yefangwong)
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

package com.hongfang.csp.webframeworx.common.api;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * <p>
 *     REST API 返回結果
 * </p>
 *
 * @author yfwong
 * @since 2020-04-29
 */
public class ApiResult<T> extends common.api.ApiResult<T> {

    public ApiResult() {
        super();
    }

    public ApiResult(int code, boolean success, String message, T data, Date time, long timestamp) {
        super(code, success, message, data, time, timestamp);
    }

    // =========================================================================
    // 兒子重寫 builder()：確保其 build() 產出的是兒子的 ApiResult<T>！
    // =========================================================================
    public static <T> WebApiResultBuilder<T> builder() {
        return new WebApiResultBuilder<>();
    }

    public static class WebApiResultBuilder<T> extends common.api.ApiResult.ApiResultBuilder<T> {
        @Override
        public WebApiResultBuilder<T> code(int code) {
            this.code = code;
            return this;
        }
        @Override
        public WebApiResultBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }
        @Override
        public WebApiResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }
        @Override
        public WebApiResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }
        @Override
        public WebApiResultBuilder<T> time(Date time) {
            this.time = time;
            return this;
        }
        @Override
        public WebApiResultBuilder<T> timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        // 核心關鍵：build() 回傳的是兒子的 ApiResult<T>！
        public ApiResult<T> build() {
            return new ApiResult<>(code, success, message, data, time, timestamp);
        }
    }

    // ========================================================
    // 協變覆寫（Covariant Override）: 將回覆型態覆寫回兒子自己
    // ========================================================
    @Override
    public ApiResult<T> setCode(int code) { super.setCode(code); return this; }
    @Override
    public ApiResult<T> setSuccess(boolean success) { super.setSuccess(success); return this; }
    @Override
    public ApiResult<T> setMessage(String message) { super.setMessage(message); return this; }
    @Override
    public ApiResult<T> setData(T data) {
        super.setData(data);
        return this;
    }

    public static <T> ApiResult<T> ok(T data) {
        return result(ApiCode.SUCCESS, data);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode, T data) {
       return result(apiCode, null, data);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode, String message, T data) {
        boolean success = false;
        if (apiCode.getCode() == ApiCode.SUCCESS.getCode()) {
            success = true;
        }
        // 修正：只有當傳進來的 message 是空白時，才拿 apiCode 的預設訊息 ("操作成功")
        if (StringUtils.isBlank(message) && apiCode != null) {
            message = apiCode.getMessage();
        }
        return (ApiResult<T>)ApiResult.builder()
                .code(apiCode.getCode())
                .message(message)
                .data(data)
                .success(success)
                .time(new Date())
                .build();
    }
}
