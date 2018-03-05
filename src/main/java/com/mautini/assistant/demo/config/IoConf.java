package com.mautini.assistant.demo.config;

public class IoConf {

    public static final String TEXT = "TEXT";

    public static final String AUDIO = "AUDIO";

    private String inputMode;

    private String outputMode;

    public IoConf() {
    }

    public String getInputMode() {
        return inputMode;
    }

    public void setInputMode(String inputMode) {
        this.inputMode = inputMode;
    }

    public String getOutputMode() {
        return outputMode;
    }

    public void setOutputMode(String outputMode) {
        this.outputMode = outputMode;
    }
}
