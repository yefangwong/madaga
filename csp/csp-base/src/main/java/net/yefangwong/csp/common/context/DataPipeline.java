/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.context;

import java.io.Serializable;
import java.util.*;

/**
 * DataPipeline - 數據傳送管道
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 萬能數據傳送管道
 * 程 式 代 號 ：DataPipeline.java
 * 公 司 ：Hongfang Intelligent Technology / yefangwong
 * 描 述 ：提供全域上下文 (GlobalContext)、強型別 DTO 與動態 Key-Value 雙軌適配提取。
 *
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public class DataPipeline implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 全域操作上下文 (包含操作者 Email, ComCode, ClientIP, Role, TraceID)
     */
    private GlobalContext context;

    /**
     * 萬能物件列表容器 (支援 Class.isInstance 自動檢索)
     */
    private final List<Object> elements = new ArrayList<>();

    /**
     * 萬能鍵值對容器 (支援具名 Key 與 Class 名稱檢索)
     */
    private final Map<String, Object> dataMap = new HashMap<>();

    public DataPipeline() {
        this.context = new GlobalContext();
    }

    public DataPipeline(GlobalContext context) {
        this.context = (context != null) ? context : new GlobalContext();
    }

    public static DataPipeline create() {
        return new DataPipeline();
    }

    public static DataPipeline of(GlobalContext context) {
        return new DataPipeline(context);
    }

    // =========================================================================
    // 多型 Fluent 寫入 (Dual-Track Fluent Writes)
    // =========================================================================
    /**
     * 軌道一：向管道注入強型別 DTO 物件 (支援 Class 標記與內部 elements 儲存)
     */
    public <T> DataPipeline put(Class<T> clazz, T payload) {
        if (clazz != null && payload != null) {
            this.dataMap.put(clazz.getName(), payload);
            if (!this.elements.contains(payload)) {
                this.elements.add(payload);
            }
        }
        return this;
    }

    /**
     * 軌道二：向管道注入帶 Key 名稱的 Payload 物件
     */
    public DataPipeline put(String key, Object item) {
        if (key != null && item != null) {
            this.dataMap.put(key, item);
            if (!this.elements.contains(item)) {
                this.elements.add(item);
            }
        }
        return this;
    }

    /**
     * 注入一般物件 (鏈式 API)
     */
    public DataPipeline add(Object item) {
        if (item != null) {
            this.elements.add(item);
        }
        return this;
    }

    /**
     * 帶 Key 的注入相容方法
     */
    public DataPipeline add(String key, Object item) {
        return put(key, item);
    }

    // =========================================================================
    // 多型強型別檢索 (Type-Safe & Dynamic Lookup)
    // =========================================================================
    /**
     * 軌道一強型別檢索：依據 Class 類型，自動尋找第一個匹配之物件
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        if (clazz == null)
            return null;
        Object obj = dataMap.get(clazz.getName());
        if (obj != null && clazz.isInstance(obj)) {
            return (T) obj;
        }
        for (Object item : elements) {
            if (clazz.isInstance(item)) {
                return (T) item;
            }
        }
        return null;
    }

    /**
     * 軌道二動態 Key 檢索：依據 Key 進行檢索並自動轉型
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (key == null)
            return null;
        return (T) dataMap.get(key);
    }

    /**
     * 依據 Key 與 Class 進行安全型別檢索
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        if (key == null || clazz == null)
            return null;
        Object val = dataMap.get(key);
        if (val != null && clazz.isInstance(val)) {
            return (T) val;
        }
        return null;
    }

    // =========================================================================
    // 管道狀態檢查與維護 (Utility Methods)
    // =========================================================================
    public boolean has(Class<?> clazz) {
        return get(clazz) != null;
    }

    public boolean has(String key) {
        return dataMap.containsKey(key);
    }

    public boolean hasPayload(Class<?> clazz) {
        return has(clazz);
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty() && dataMap.isEmpty();
    }

    public void clear() {
        this.elements.clear();
        this.dataMap.clear();
    }

    // =========================================================================
    // Getters & Setters
    // =========================================================================
    public GlobalContext getContext() {
        return context;
    }

    public DataPipeline setContext(GlobalContext context) {
        this.context = (context != null) ? context : new GlobalContext();
        return this;
    }

    public List<Object> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public Map<String, Object> getDataMap() {
        return Collections.unmodifiableMap(dataMap);
    }

    @Override
    public String toString() {
        return "DataPipeline{" +
                "context=" + context +
                ", elementCount=" + elements.size() +
                ", mapKeyCount=" + dataMap.size() +
                '}';
    }
}
