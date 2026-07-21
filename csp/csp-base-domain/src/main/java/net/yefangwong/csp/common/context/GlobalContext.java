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
 * 原創 GlobalContext - 全域操作上下文
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 核心全域上下文
 * 程 式 代 號 ：GlobalContext.java
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：提供全系統跨層傳遞之操作者 Email、公司代碼 (comCode)、客戶端 IP (clientIp)、
 *                  角色 (role) 與 TraceID 鏈路追蹤上下文
 *
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public class GlobalContext implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 操作者帳號 / Email
     */
    private String operatorEmail;

    /**
     * 公司 / 機構代碼
     */
    private String comCode;

    /**
     * 客戶端 IP 位址
     */
    private String clientIp;

    /**
     * 操作者角色權限
     */
    private String role;

    /**
     * 鏈路追蹤 ID (TraceID)
     */
    private String traceId;

    public GlobalContext() {
        this.traceId = generateTraceId();
    }

    public GlobalContext(String operatorEmail, String comCode, String clientIp, String role) {
        this(operatorEmail, comCode, clientIp, role, generateTraceId());
    }

    public GlobalContext(String operatorEmail, String comCode, String clientIp, String role, String traceId) {
        this.operatorEmail = operatorEmail;
        this.comCode = comCode;
        this.clientIp = clientIp;
        this.role = role;
        this.traceId = (traceId != null && !traceId.trim().isEmpty()) ? traceId : generateTraceId();
    }

    private static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 工廠靜態方法
    public static GlobalContext of(String operatorEmail, String comCode) {
        return new GlobalContext(operatorEmail, comCode, "127.0.0.1", "USER");
    }

    public static GlobalContext of(String operatorEmail, String comCode, String clientIp, String role) {
        return new GlobalContext(operatorEmail, comCode, clientIp, role);
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

        public GlobalContext build() {
            return new GlobalContext(operatorEmail, comCode, clientIp, role, traceId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalContext context = (GlobalContext) o;
        return Objects.equals(operatorEmail, context.operatorEmail) &&
                Objects.equals(comCode, context.comCode) &&
                Objects.equals(clientIp, context.clientIp) &&
                Objects.equals(role, context.role) &&
                Objects.equals(traceId, context.traceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operatorEmail, comCode, clientIp, role, traceId);
    }

    @Override
    public String toString() {
        return "GlobalContext{" +
                "operatorEmail='" + operatorEmail + '\'' +
                ", comCode='" + comCode + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", role='" + role + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
