/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 原創 AppErrors - 多重錯誤累積與鏈式診斷容器
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 核心診斷容器
 * 程 式 代 號 ：AppErrors.java
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：提供多重 AppError 累積、鏈式調用(Fluent API)與診斷輔助方法
 *
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public class AppErrors implements Serializable, Iterable<AppError> {
    private static final long serialVersionUID = 1L;

    private final List<AppError> errors = new ArrayList<>();

    public AppErrors() {
    }

    public static AppErrors create() {
        return new AppErrors();
    }

    public static AppErrors of(AppError... errorArray) {
        AppErrors appErrors = new AppErrors();
        if (errorArray != null) {
            for (AppError err : errorArray) {
                appErrors.add(err);
            }
        }
        return appErrors;
    }

    public AppErrors add(AppError error) {
        if (error != null) {
            this.errors.add(error);
        }
        return this;
    }

    public AppErrors add(String code, String message) {
        return add(AppError.of(code, message));
    }

    public AppErrors add(String code, String message, String field) {
        return add(AppError.of(code, message, field));
    }

    public AppErrors addValidation(String field, String message) {
        return add(AppError.validation(field, message));
    }

    public AppErrors addAll(AppErrors other) {
        if (other != null && other.hasErrors()) {
            this.errors.addAll(other.getErrors());
        }
        return this;
    }

    public AppErrors addAll(Iterable<AppError> otherErrors) {
        if (otherErrors != null) {
            for (AppError err : otherErrors) {
                add(err);
            }
        }
        return this;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean isEmpty() {
        return errors.isEmpty();
    }

    public int size() {
        return errors.size();
    }

    public List<AppError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public String getFirstMessage() {
        if (hasErrors()) {
            return errors.get(0).getMessage();
        }
        return null;
    }

    public void clear() {
        this.errors.clear();
    }

    @Override
    public Iterator<AppError> iterator() {
        return getErrors().iterator();
    }

    @Override
    public String toString() {
        return "AppErrors{" +
                "count=" + errors.size() +
                ", errors=" + errors +
                '}';
    }
}
