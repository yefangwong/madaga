/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.compute;

/**
 * CapabilityRouter - 能力導向動態算力分流器
 */
public class CapabilityRouter implements IComputeResourceProxy {

    private final LocalHeterogeneousDriver localDriver = new LocalHeterogeneousDriver();
    private final NvidiaDgxSparkDriver dgxDriver = new NvidiaDgxSparkDriver();
    private final ForbiddenVectorInterceptor interceptor = new ForbiddenVectorInterceptor();

    @Override
    public ComputeResult submitInferenceTask(ComputeTaskData taskData) {
        if (taskData == null) {
            return ComputeResult.failure("CapabilityRouter", "ComputeTaskData cannot be null");
        }

        // 0. Pre-Flight 禁區向量預檢 (微秒級零成本攔截 80% 失敗負樣本)
        if (interceptor.isForbiddenVector(taskData.getVectorEmbedding())) {
            return ComputeResult.blocked("Forbidden vector region detected, bypassing computation.");
        }

        // 1. VRAM 能力動態分流
        if (taskData.getRequiredVramGb() > 16) {
            return dgxDriver.submitInferenceTask(taskData);
        } else {
            return localDriver.submitInferenceTask(taskData);
        }
    }

    @Override
    public ComputeResult optimizeKernel(KernelForgeTask task) {
        return dgxDriver.optimizeKernel(task);
    }
}
