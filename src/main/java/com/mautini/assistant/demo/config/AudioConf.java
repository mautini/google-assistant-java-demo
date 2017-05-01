package com.mautini.assistant.demo.config;

public class AudioConf {

    private Integer sampleRate;

    private Integer sampleSizeInBits;

    private Integer channels;

    private Boolean signed;

    private Boolean bigEndian;

    public AudioConf() {
    }

    public Integer getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Integer getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public void setSampleSizeInBits(Integer sampleSizeInBits) {
        this.sampleSizeInBits = sampleSizeInBits;
    }

    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    public Boolean getSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public Boolean getBigEndian() {
        return bigEndian;
    }

    public void setBigEndian(Boolean bigEndian) {
        this.bigEndian = bigEndian;
    }
}
