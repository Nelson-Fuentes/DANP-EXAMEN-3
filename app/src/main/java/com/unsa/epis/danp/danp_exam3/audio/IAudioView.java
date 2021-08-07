package com.unsa.epis.danp.danp_exam3.audio;

import android.content.Context;

import com.unsa.epis.danp.danp_exam3.HandlerException;

public interface IAudioView extends HandlerException {
    boolean verify_permissions(String[] permissions);
    void request_permissions(String[] permissions, int code);
    Context get_context();
    void listening_initialized();
    void turn_on_detect_clap();
    void turn_off_detect_clap();
    void turn_on_detect_whistle();
    void turn_off_detect_whistle();
}
