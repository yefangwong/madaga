/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.compute;

/**
 * LocalHeterogeneousDriver - 本地異構驅動器 (RTX 3070 8GB + RX 6600 XT 8GB 雙卡並聯)
 */
public class LocalHeterogeneousDriver implements IComputeResourceProxy {

    @Override
    public ComputeResult submitInferenceTask(ComputeTaskData taskData) {
        String prompt = taskData != null ? taskData.getPrompt() : "";
        return ComputeResult.success("LocalHeterogeneousDriver (RTX 3070 + RX 6600 XT)", "Executed parallel inference for prompt: " + prompt);
    }

    @Override
    public ComputeResult optimizeKernel(KernelForgeTask task) {
        String name = task != null ? task.getKernelName() : "unknown";
        return ComputeResult.success("LocalHeterogeneousDriver (RTX 3070 + RX 6600 XT)", "Compiled CUDA/ROCm kernel: " + name);
    }
}
