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
 * ComputeTaskData - 算力推論任務與需求模型
 */
public class ComputeTaskData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String taskId;
    private double[] vectorEmbedding;
    private int requiredVramGb;
    private String prompt;

    public ComputeTaskData() {}

    public ComputeTaskData(String taskId, double[] vectorEmbedding, int requiredVramGb, String prompt) {
        this.taskId = taskId;
        this.vectorEmbedding = vectorEmbedding;
        this.requiredVramGb = requiredVramGb;
        this.prompt = prompt;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public double[] getVectorEmbedding() {
        return vectorEmbedding;
    }

    public void setVectorEmbedding(double[] vectorEmbedding) {
        this.vectorEmbedding = vectorEmbedding;
    }

    public int getRequiredVramGb() {
        return requiredVramGb;
    }

    public void setRequiredVramGb(int requiredVramGb) {
        this.requiredVramGb = requiredVramGb;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
