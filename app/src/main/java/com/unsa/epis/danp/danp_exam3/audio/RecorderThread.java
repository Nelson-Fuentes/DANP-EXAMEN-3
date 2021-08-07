package com.unsa.epis.danp.danp_exam3.audio;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class RecorderThread extends Thread {

    private AudioRecord audio_record;
    private boolean is_recording;
    private int channel_configuration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audio_encoding = AudioFormat.ENCODING_PCM_16BIT;
    private int sample_rate = 44100;
    private int frame_byte_size = 2048;
    byte[] buffer;

    public RecorderThread(){
        int rec_buf_size = AudioRecord.getMinBufferSize(
                sample_rate,
                channel_configuration,
                audio_encoding
        );
        audio_record = new AudioRecord(MediaRecorder.AudioSource.MIC, sample_rate, channel_configuration, audio_encoding, rec_buf_size);
        buffer = new byte[frame_byte_size];
    }

    public AudioRecord get_audio_record(){
        return audio_record;
    }

    public boolean is_is_recording(){
        return this.isAlive() && is_recording;
    }

    public void start_recording(){
        try{
            audio_record.startRecording();
            is_recording = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop_recording(){
        try{
            audio_record.stop();
            is_recording = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] get_frame_bytes(){
        audio_record.read(buffer, 0, frame_byte_size);
        int total_abs_value = 0;
        short sample = 0;
        float average_abs_value = 0.0f;
        for (int i = 0; i < frame_byte_size; i += 2) {
            sample = (short)((buffer[i]) | buffer[i + 1] << 8);
            total_abs_value += Math.abs(sample);
        }
        average_abs_value = total_abs_value / frame_byte_size / 2;
        if (average_abs_value < 30){
            return null;
        }

        return buffer;
    }

    public void run() {
        start_recording();
    }
}
