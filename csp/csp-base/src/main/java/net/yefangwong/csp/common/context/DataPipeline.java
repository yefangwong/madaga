/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * DataPipeline - 萬能傳參管道
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 萬能傳參管道
 * 程 式 代 號 ：DataPipeline.java
 * 公 司 ：Hongfang Intelligent Technology / yefangwong
 * 描 述 ：將全域上下文 (GlobalContext) 與強型別 Payload 統一打平，
 * 實現 BaseBL 簽名 100% 穩定性 (Signature Stability)
 *
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public class DataPipeline implements Serializable {
    private static final long serialVersionUID = 1L;

    private GlobalContext context;
    private final Map<Class<?>, Object> payloadMap = new HashMap<>();

    public DataPipeline() {
    }

    public DataPipeline(GlobalContext context) {
        this.context = context;
    }

    public GlobalContext getContext() {
        return context;
    }

    public void setContext(GlobalContext context) {
        this.context = context;
    }

    public <T> void put(Class<T> clazz, T payload) {
        if (clazz != null && payload != null) {
            payloadMap.put(clazz, payload);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        return (T) payloadMap.get(clazz);
    }

    public boolean hasPayload(Class<?> clazz) {
        return payloadMap.containsKey(clazz);
    }
}
