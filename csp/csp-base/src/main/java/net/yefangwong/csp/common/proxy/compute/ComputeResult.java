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
 * ComputeResult - 算力執行結果模型
 */
public class ComputeResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private boolean blocked;
    private String driverName;
    private String outputText;
    private String errorMessage;

    public ComputeResult() {}

    public ComputeResult(boolean success, boolean blocked, String driverName, String outputText, String errorMessage) {
        this.success = success;
        this.blocked = blocked;
        this.driverName = driverName;
        this.outputText = outputText;
        this.errorMessage = errorMessage;
    }

    public static ComputeResult success(String driverName, String outputText) {
        return new ComputeResult(true, false, driverName, outputText, null);
    }

    public static ComputeResult blocked(String message) {
        return new ComputeResult(false, true, "Pre-Flight Interceptor", null, message);
    }

    public static ComputeResult failure(String driverName, String errorMessage) {
        return new ComputeResult(false, false, driverName, null, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOutputText() {
        return outputText;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
