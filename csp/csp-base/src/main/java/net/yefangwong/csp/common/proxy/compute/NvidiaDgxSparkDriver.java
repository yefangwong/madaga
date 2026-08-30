/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.compute;

/**
 * NvidiaDgxSparkDriver - NVIDIA DGX Spark 專用驅動器 (128GB Unified Memory 節點)
 */
public class NvidiaDgxSparkDriver implements IComputeResourceProxy {

    @Override
    public ComputeResult submitInferenceTask(ComputeTaskData taskData) {
        String prompt = taskData != null ? taskData.getPrompt() : "";
        return ComputeResult.success("NvidiaDgxSparkDriver (128GB Unified Memory)", "Executed remote DGX Spark task for prompt: " + prompt);
    }

    @Override
    public ComputeResult optimizeKernel(KernelForgeTask task) {
        String name = task != null ? task.getKernelName() : "unknown";
        return ComputeResult.success("NvidiaDgxSparkDriver (128GB Unified Memory)", "Optimized MCTS kernel on DGX Spark node: " + name);
    }
}
