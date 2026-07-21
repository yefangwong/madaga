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
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：提供全域上下文 (GlobalContext)、多筆物件 List 列表與 Key-Value Map 雙重適配提取。
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
     * 萬能物件列表容器 (類似 VData 列表，支援強型別 Class.isInstance 自動檢索)
     */
    private final List<Object> elements = new ArrayList<>();

    /**
     * 萬能鍵值對容器 (支援具名 key 檢索)
     */
    private final Map<String, Object> dataMap = new HashMap<>();

    /**
     * 預設建構子 (自動初始化預設 GlobalContext)
     */
    public DataPipeline() {
        this.context = new GlobalContext();
    }

    /**
     * 帶全域上下文之建構子
     *
     * @param context 全域操作上下文
     */
    public DataPipeline(GlobalContext context) {
        this.context = (context != null) ? context : new GlobalContext();
    }

    // =========================================================================
    // 靜態工廠建構方法 (Factory Methods)
    // =========================================================================
    /**
     * 建立空白管道
     *
     * @return 新的 DataPipeline 實例
     */
    public static DataPipeline create() {
        return new DataPipeline();
    }

    /**
     * 以指定之 GlobalContext 建立管道
     *
     * @param context 全域操作上下文
     * @return 新的 DataPipeline 實例
     */
    public static DataPipeline of(GlobalContext context) {
        return new DataPipeline(context);
    }

    // =========================================================================
    // 鏈式資料寫入 (Fluent API Write)
    // =========================================================================
    /**
     * 向管道注入一個 Payload 物件
     *
     * @param item 任意 Payload 物件
     * @return 當前 DataPipeline 實例 (支援鏈式呼叫)
     */
    public DataPipeline add(Object item) {
        if (item != null) {
            this.elements.add(item);
        }
        return this;
    }

    /**
     * 向管道注入一個帶 Key 名稱的 Payload 物件
     *
     * @param key  檢索鍵值
     * @param item 任意 Payload 物件
     * @return 當前 DataPipeline 實例 (支援鏈式呼叫)
     */
    public DataPipeline add(String key, Object item) {
        if (key != null && item != null) {
            this.dataMap.put(key, item);
            if (!this.elements.contains(item)) {
                this.elements.add(item);
            }
        }
        return this;
    }

    // =========================================================================
    // 核心強型別檢索 (Type-Safe Lookup - 免手動 (Type Cast) 轉型)
    // =========================================================================
    /**
     * 核心強型別檢索：依據 Class 類型，自動在管道中尋找第一個匹配的物件並安全轉型
     *
     * @param <T>   目標型別泛型
     * @param clazz 目標型別 Class 標籤 (例如 UserVO.class)
     * @return 匹配之物件實例；若未找到則回傳 null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        if (clazz == null) return null;
        for (Object item : elements) {
            if (clazz.isInstance(item)) {
                return (T) item;
            }
        }
        return null;
    }

    /**
     * 依據 Key 與 Class 進行具名強型別檢索
     *
     * @param <T>   目標型別泛型
     * @param key   檢索鍵值
     * @param clazz 目標型別 Class 標籤
     * @return 匹配之物件實例；若未找到或型別不符則回傳 null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        if (key == null || clazz == null) return null;
        Object val = dataMap.get(key);
        if (val != null && clazz.isInstance(val)) {
            return (T) val;
        }
        return null;
    }

    // =========================================================================
    // 管道狀態檢查與維護 (Utility Methods)
    // =========================================================================
    /**
     * 檢查管道中是否包含指定型別之物件
     *
     * @param clazz 目標型別 Class
     * @return true 代表存在；false 代表不存在
     */
    public boolean has(Class<?> clazz) {
        return get(clazz) != null;
    }

    /**
     * 檢查管道中是否包含指定 Key 之物件
     *
     * @param key 檢索鍵值
     * @return true 代表存在；false 代表不存在
     */
    public boolean has(String key) {
        return dataMap.containsKey(key);
    }

    /**
     * 取得管道目前儲存的物件數量
     *
     * @return 物件總筆數
     */
    public int size() {
        return elements.size();
    }

    /**
     * 檢查管道是否完全為空
     *
     * @return true 代表無任何資料；false 代表有資料
     */
    public boolean isEmpty() {
        return elements.isEmpty() && dataMap.isEmpty();
    }

    /**
     * 清空管道內部所有資料
     */
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
