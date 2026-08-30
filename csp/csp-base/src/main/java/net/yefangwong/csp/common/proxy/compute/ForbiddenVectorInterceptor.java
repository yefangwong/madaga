/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.compute;

/**
 * ForbiddenVectorInterceptor - Pre-Flight 80% 負樣本微秒級零成本預檢門禁
 */
public class ForbiddenVectorInterceptor {

    /**
     * 檢測向量是否落入禁區 (Forbidden Vector Region)
     *
     * @param vectorEmbedding 特徵向量
     * @return true 代表落入禁區 (需微秒級零成本攔截)；false 代表安全
     */
    public boolean isForbiddenVector(double[] vectorEmbedding) {
        if (vectorEmbedding == null || vectorEmbedding.length == 0) {
            return false;
        }
        // 假設特徵向量首元素為負值 (-1.0) 代表已知的 80% 失敗負樣本禁區
        return vectorEmbedding[0] < -0.5;
    }
}
