/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.context;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * GlobalContext - 平台全域上下文模型
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 全域上下文
 * 程 式 代 號 ：GlobalContext.java
 * 公 司 ：Hongfang Intelligent Technology / yefangwong
 * 描 述 ：封裝操作者 Email、機構/專案代碼 (comCode)、用戶 IP、角色 (role)、Trace ID 與租戶 ID
 *
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public class GlobalContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private String operatorEmail;
    private String comCode;
    private String clientIp;
    private String role;
    private String traceId;
    private String tenantId;

    public GlobalContext() {
        this.traceId = generateTraceId();
        this.tenantId = "default-tenant";
        this.clientIp = "127.0.0.1";
        this.role = "USER";
    }

    public GlobalContext(String operatorEmail, String comCode, String clientIp, String traceId, String tenantId) {
        this.operatorEmail = operatorEmail;
        this.comCode = comCode;
        this.clientIp = (clientIp != null) ? clientIp : "127.0.0.1";
        this.traceId = (traceId != null && !traceId.trim().isEmpty()) ? traceId : generateTraceId();
        this.tenantId = (tenantId != null) ? tenantId : "default-tenant";
        this.role = "USER";
    }

    public GlobalContext(String operatorEmail, String comCode, String clientIp, String role, String traceId,
            String tenantId) {
        this.operatorEmail = operatorEmail;
        this.comCode = comCode;
        this.clientIp = (clientIp != null) ? clientIp : "127.0.0.1";
        this.role = (role != null) ? role : "USER";
        this.traceId = (traceId != null && !traceId.trim().isEmpty()) ? traceId : generateTraceId();
        this.tenantId = (tenantId != null) ? tenantId : "default-tenant";
    }

    private static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 靜態工廠方法
    public static GlobalContext of(String operatorEmail, String comCode) {
        return new GlobalContext(operatorEmail, comCode, "127.0.0.1", generateTraceId(), "default-tenant");
    }

    public static GlobalContext of(String operatorEmail, String comCode, String clientIp, String role) {
        return new GlobalContext(operatorEmail, comCode, clientIp, role, generateTraceId(), "default-tenant");
    }

    public static GlobalContextBuilder builder() {
        return new GlobalContextBuilder();
    }

    public static class GlobalContextBuilder {
        private String operatorEmail;
        private String comCode;
        private String clientIp = "127.0.0.1";
        private String role = "USER";
        private String traceId;
        private String tenantId = "default-tenant";

        public GlobalContextBuilder operatorEmail(String operatorEmail) {
            this.operatorEmail = operatorEmail;
            return this;
        }

        public GlobalContextBuilder comCode(String comCode) {
            this.comCode = comCode;
            return this;
        }

        public GlobalContextBuilder clientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }

        public GlobalContextBuilder role(String role) {
            this.role = role;
            return this;
        }

        public GlobalContextBuilder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public GlobalContextBuilder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public GlobalContext build() {
            return new GlobalContext(operatorEmail, comCode, clientIp, role, traceId, tenantId);
        }
    }

    // Getters & Setters
    public String getOperatorEmail() {
        return operatorEmail;
    }

    public GlobalContext setOperatorEmail(String operatorEmail) {
        this.operatorEmail = operatorEmail;
        return this;
    }

    public String getComCode() {
        return comCode;
    }

    public GlobalContext setComCode(String comCode) {
        this.comCode = comCode;
        return this;
    }

    public String getClientIp() {
        return clientIp;
    }

    public GlobalContext setClientIp(String clientIp) {
        this.clientIp = clientIp;
        return this;
    }

    public String getRole() {
        return role;
    }

    public GlobalContext setRole(String role) {
        this.role = role;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public GlobalContext setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public GlobalContext setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GlobalContext that = (GlobalContext) o;
        return Objects.equals(operatorEmail, that.operatorEmail) &&
                Objects.equals(comCode, that.comCode) &&
                Objects.equals(traceId, that.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operatorEmail, comCode, traceId);
    }

    @Override
    public String toString() {
        return "GlobalContext{" +
                "operatorEmail='" + operatorEmail + '\'' +
                ", comCode='" + comCode + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", role='" + role + '\'' +
                ", traceId='" + traceId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
