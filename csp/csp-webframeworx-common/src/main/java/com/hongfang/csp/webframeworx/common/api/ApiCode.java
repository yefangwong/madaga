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

/**
 * <p>
 *     REST API 結果回傳碼
 * </p>
 *
 * @author yfwong
 * @since 2020-04-28
 */
public enum ApiCode {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 非法訪問
     */
    UNAUTHORIZED(401, "非法訪問"),

    /**
     * 沒有權限
     */
    NOT_PERMISSION(403, "沒有權限"),

    /**
     * 您請求的資源不存在
     */
    NOT_FOUND(404, "您請求的資源不存在"),

    /**
     * 操作失敗
     */
    FAIL(500, "操作失敗");


    private final int code;
    private final String message;

    ApiCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
