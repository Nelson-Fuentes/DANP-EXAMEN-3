package com.unsa.epis.danp.danp_exam3.audio;


import android.media.AudioFormat;
import android.media.AudioRecord;

import com.musicg.api.ClapApi;
import com.musicg.api.WhistleApi;
import com.musicg.wave.WaveHeader;


public class DetectorThread extends Thread {

    private RecorderThread recorder;
    private WaveHeader wave_header;
    private ClapApi clapApi;
    private volatile Thread _thread;
    private boolean detect_clap;
    private boolean detect_whistle;
    private OnSoundDetected sound_detected;
    private WhistleApi whistleApi;


    public DetectorThread(RecorderThread recorder, OnSoundDetected sound_detected) {
        this.recorder = recorder;
        this.sound_detected = sound_detected;
        AudioRecord audioRecord = recorder.get_audio_record();

        int bits_per_sample = 0;
        if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT) {
            bits_per_sample = 16;
        } else if (audioRecord.getAudioFormat() == AudioFormat.ENCODING_PCM_8BIT) {
            bits_per_sample = 8;
        }
        int channel = 0;
        if (audioRecord.getChannelConfiguration() == AudioFormat.CHANNEL_IN_MONO) {
            channel = 1;
        }
        wave_header = new WaveHeader();
        wave_header.setChannels(channel);
        wave_header.setBitsPerSample(bits_per_sample);
        wave_header.setSampleRate(audioRecord.getSampleRate());
        clapApi = new ClapApi(wave_header);
        whistleApi = new WhistleApi(wave_header);
    }

    public void set_detect_clap(boolean detect_clap){
        this.detect_clap = detect_clap;
    }

    public void set_detect_whistle(boolean detect_whistle){
        this.detect_whistle = detect_whistle;
    }

    public void start() {
        _thread = new Thread(this);
        _thread.start();
    }

    public void stop_detection() {
        _thread = null;
    }

    public void run() {
        byte[] buffer;

        Thread thisThread = Thread.currentThread();
        while (_thread == thisThread) {
            buffer = recorder.get_frame_bytes();
            if (buffer != null) {
                if (this.detect_clap && clapApi.isClap(buffer)){
                    this.sound_detected.sound_detected();
                } else if (this.detect_whistle && whistleApi.isWhistle(buffer)){
                    this.sound_detected.sound_detected();
                }

            }
        }
        
    }
}
