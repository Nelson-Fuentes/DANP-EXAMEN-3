package com.unsa.epis.danp.danp_exam3.audio;

public interface IAudioPresenter {
    void initialize();
    void turn_off_detect_clap();
    void turn_on_detect_clap();
    void turn_on_detect_whistle();
    void turn_off_detect_whistle();
}
