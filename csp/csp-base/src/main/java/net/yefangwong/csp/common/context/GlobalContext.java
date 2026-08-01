/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.context;

import java.io.Serializable;

/**
 * GlobalContext - 平台全域上下文模型
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 全域上下文
 * 程 式 代 號 ：GlobalContext.java
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：封裝操作者 Email、機構/專案代碼 (comCode)、用戶 IP、Trace ID 與租戶 ID
 *
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public class GlobalContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private String operatorEmail;
    private String comCode;
    private String clientIp;
    private String traceId;
    private String tenantId;

    public GlobalContext() {}

    public GlobalContext(String operatorEmail, String comCode, String clientIp, String traceId, String tenantId) {
        this.operatorEmail = operatorEmail;
        this.comCode = comCode;
        this.clientIp = clientIp;
        this.traceId = traceId;
        this.tenantId = tenantId;
    }

    public String getOperatorEmail() {
        return operatorEmail;
    }

    public void setOperatorEmail(String operatorEmail) {
        this.operatorEmail = operatorEmail;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
