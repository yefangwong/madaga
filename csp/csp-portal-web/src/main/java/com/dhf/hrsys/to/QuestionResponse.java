package com.dhf.hrsys.to;

public class QuestionResponse {
    private String answer;
    private boolean success;

    // Constructors, getters, and setters

    public QuestionResponse(String answer, boolean success) {
        this.answer = answer;
        this.success = success;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
