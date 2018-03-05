package com.mautini.assistant.demo.config;

public class IoConf {

    public static final String TEXT = "TEXT";

    public static final String AUDIO = "AUDIO";

    private String inputMode;

    private Boolean outputAudio;

    public IoConf() {
    }

    public String getInputMode() {
        return inputMode;
    }

    public void setInputMode(String inputMode) {
        this.inputMode = inputMode;
    }

    public Boolean getOutputAudio() {
        return outputAudio;
    }

    public void setOutputAudio(Boolean outputAudio) {
        this.outputAudio = outputAudio;
    }
}
