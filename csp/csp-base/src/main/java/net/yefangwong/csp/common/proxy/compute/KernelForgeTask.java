/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.proxy.compute;

import java.io.Serializable;

/**
 * KernelForgeTask - CUDA / ROCm 運算子優化任務模型
 */
public class KernelForgeTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private String kernelName;
    private String sourceCode;

    public KernelForgeTask() {}

    public KernelForgeTask(String kernelName, String sourceCode) {
        this.kernelName = kernelName;
        this.sourceCode = sourceCode;
    }

    public String getKernelName() {
        return kernelName;
    }

    public void setKernelName(String kernelName) {
        this.kernelName = kernelName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
