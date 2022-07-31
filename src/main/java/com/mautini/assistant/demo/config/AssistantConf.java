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

    @SuppressWarnings("unused")
    public void setAssistantApiEndpoint(String assistantApiEndpoint) {
        this.assistantApiEndpoint = assistantApiEndpoint;
    }

    public Integer getAudioSampleRate() {
        return audioSampleRate;
    }

    @SuppressWarnings("unused")
    public void setAudioSampleRate(Integer audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public Integer getChunkSize() {
        return chunkSize;
    }

    @SuppressWarnings("unused")
    public void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize;
    }

    public Integer getVolumePercent() {
        return volumePercent;
    }

    @SuppressWarnings("unused")
    public void setVolumePercent(Integer volumePercent) {
        this.volumePercent = volumePercent;
    }
}