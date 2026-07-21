/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.bl;

import common.api.ApiResult;
import net.yefangwong.csp.common.error.AppErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseBL - 業務邏輯層 5 大生命週期抽象基類
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 業務邏輯基底
 * 程 式 代 號 ：BaseBL.java
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：規範 5 大生命週期工序（防呆 -> 權限 -> 核心處理 -> ISO 27001 審計 -> API 回傳），
 *                  內建 AppErrors 診斷容器，兼具強型別 DTO 與 DataPipeline 萬能傳參靈活性，
 *                  無縫對齊 MyBatis 與 Spring 聲明式事務管理
 *
 * @param <REQ>  請求 Payload 物件型別 (Request DTO 或 DataPipeline 萬能管道)
 * @param <RESP> 響應 Payload 物件型別 (Response DTO)
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public abstract class BaseBL<REQ, RESP> {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 內建 AppErrors 診斷容器 (零共享狀態，線程安全)
     */
    protected final AppErrors errors = new AppErrors();

    // =========================================================================
    // 5 大抽象/Hook 生命週期工序 (Lifecycle Steps)
    // =========================================================================

    /**
     * 1. 前置輸入防呆校驗 (回傳 boolean 簡明判斷)
     *
     * @param request 請求 Payload 物件 (REQ)
     * @return true 代表校驗通過；false 代表失敗 (並將錯誤細節推入 errors)
     */
    protected abstract boolean validateInput(REQ request);

    /**
     * 2. 權限與操作者身分校驗 (回傳 boolean 簡明判斷)
     *
     * @param request       請求 Payload 物件 (REQ)
     * @param operatorEmail 操作者 Email 或帳號識別
     * @return true 代表權限核可；false 代表拒絕存取 (並將錯誤細節推入 errors)
     */
    protected abstract boolean verifyAuthority(REQ request, String operatorEmail);

    /**
     * 3. 核心業務邏輯處理 (直連 MyBatis Mapper 或持久層)
     *
     * @param request 請求 Payload 物件 (REQ)
     * @return 處理結果 Payload 物件 (RESP)
     * @throws Exception 業務例外或 SQL 執行異常
     */
    protected abstract RESP executeBusiness(REQ request) throws Exception;

    /**
     * 4. ISO 27001 審計軌跡寫入 (預設 Hook，子類別可視需求覆寫)
     *
     * @param repoId        目標儲存庫或實體 ID
     * @param operatorEmail 操作者帳號
     * @param actionCode    操作動作代碼 (e.g. "PATCH_APPROVE")
     * @param details       詳細審計描述或狀態
     * @return true 代表寫入成功；false 代表記錄失敗
     */
    protected boolean writeAuditLog(String repoId, String operatorEmail, String actionCode, String details) {
        log.info("[AUDIT-TRAIL] Action: {}, Operator: {}, Target: {}, Details: {}",
                actionCode, operatorEmail, repoId, details);
        return true;
    }

    // =========================================================================
    // 樣板主方法 (Template Method)
    // =========================================================================

    /**
     * 執行完整 5 大生命週期工序
     *
     * @param request       請求 Payload 物件 (REQ)
     * @param operatorEmail 操作者帳號
     * @param actionCode    操作動作代碼
     * @param repoId        目標儲存庫或實體 ID
     * @return 標準 ApiResult<RESP> 封裝響應
     */
    public ApiResult<RESP> process(REQ request, String operatorEmail, String actionCode, String repoId) {
        errors.clear();

        // 1. 前置輸入防呆
        if (!validateInput(request)) {
            String msg = errors.hasErrors() ? errors.getFirstMessage() : "輸入參數校驗失敗";
            log.warn("Validation failed for action [{}], operator [{}]: {}", actionCode, operatorEmail, msg);
            return ApiResult.failure(400, msg);
        }

        // 2. 權限校對
        if (!verifyAuthority(request, operatorEmail)) {
            String msg = errors.hasErrors() ? errors.getFirstMessage() : "操作權限不足";
            log.warn("Authority verification failed for action [{}], operator [{}]: {}", actionCode, operatorEmail, msg);
            return ApiResult.failure(403, msg);
        }

        try {
            // 3. 核心業務邏輯 (直連 MyBatis Mapper)
            RESP response = executeBusiness(request);

            // 4. ISO 27001 審計軌跡
            writeAuditLog(repoId, operatorEmail, actionCode, "SUCCESS");

            // 5. 響應封裝
            return ApiResult.success(response);
        } catch (Exception e) {
            log.error("Execution exception in BaseBL process for action [" + actionCode + "]", e);
            writeAuditLog(repoId, operatorEmail, actionCode, "FAILED: " + e.getMessage());
            return ApiResult.failure(500, "業務處理異常: " + e.getMessage());
        }
    }

    public AppErrors getErrors() {
        return errors;
    }
}
