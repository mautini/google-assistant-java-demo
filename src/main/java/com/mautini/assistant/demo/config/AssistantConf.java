package com.mautini.assistant.demo.config;

public class AssistantConf {

    private String assistantApiEndpoint;

    private Integer audioSampleRate;

    private Integer chunkSize;

    private Integer volumePercent;

    public AssistantConf() {
    }

    public String getAssistantApiEndpoint() {
        return assistantApiEndpoint;
    }

    public void setAssistantApiEndpoint(String assistantApiEndpoint) {
        this.assistantApiEndpoint = assistantApiEndpoint;
    }

    public Integer getAudioSampleRate() {
        return audioSampleRate;
    }

    public void setAudioSampleRate(Integer audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getVolumePercent() {
        return volumePercent;
    }

    public void setVolumePercent(Integer volumePercent) {
        this.volumePercent = volumePercent;
    }
}