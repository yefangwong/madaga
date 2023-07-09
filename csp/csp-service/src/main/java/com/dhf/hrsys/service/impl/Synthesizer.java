package com.dhf.hrsys.service.impl;


public class Synthesizer {
    private static Synthesizer instance;
    public static Synthesizer getInstance() {
        if (instance == null)
            instance = new Synthesizer();
        return instance;
    }

    public String synthesize(String text) {
        return genderPrompt(text);
    }

    private String genderPrompt(String text) {
        return text + ",性別：女";
    }
}
