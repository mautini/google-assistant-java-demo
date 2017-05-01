package com.mautini.assistant.demo.client.audio;

import com.mautini.assistant.demo.config.AudioConf;
import com.mautini.assistant.demo.exception.AudioException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioPlayer {

    private AudioConf audioConf;

    public AudioPlayer(AudioConf audioConf) {
        this.audioConf = audioConf;
    }

    public void play(byte[] sound) throws AudioException {
        try {
            AudioFormat format = AudioUtil.getAudioFormat(audioConf);
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();
            speakers.write(sound, 0, sound.length);
            speakers.drain();
            speakers.close();
        } catch (Exception e) {
            throw new AudioException("Unable to play the response", e);
        }
    }
}
