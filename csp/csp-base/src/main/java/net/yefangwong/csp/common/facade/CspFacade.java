/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.facade;

import common.api.ApiResult;
import net.yefangwong.csp.common.context.DataPipeline;

/**
 * CspFacade - 平台純無狀態 PureMVC 門面抽象類別
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) PureMVC 門面抽象
 * 程 式 代 號 ：CspFacade.java
 * 公 司 ：Hongfang Intelligent Technology / yefangwong
 * 描 述 ：為平台與所有子系統 (如 PatchVerify) 提供無狀態 PureMVC 門面基底。
 * 平台底座 100% 業務中立，不引用任何具體業務包。
 *
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public abstract class CspFacade {

    /**
     * 無狀態單例門面入口：派發 DataPipeline 請求至具體業務邏輯單元 (BaseBL)
     *
     * @param pipeline   萬能傳參管道 (含有 GlobalContext 與 Request Payload)
     * @param actionCode 業務操作代碼
     * @param <RESP>     響應 Payload 型別
     * @return ApiResult<RESP>
     */
    public abstract <RESP> ApiResult<RESP> execute(DataPipeline pipeline, String actionCode);
}
