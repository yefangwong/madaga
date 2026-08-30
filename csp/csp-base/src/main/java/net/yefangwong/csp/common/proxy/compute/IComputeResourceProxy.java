/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.compute;

/**
 * IComputeResourceProxy - 算力代理中樞介面
 */
public interface IComputeResourceProxy {
    ComputeResult submitInferenceTask(ComputeTaskData taskData);
    ComputeResult optimizeKernel(KernelForgeTask task);
}
