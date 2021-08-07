package com.unsa.epis.danp.danp_exam3.camera;

import android.content.Context;

import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;

import com.unsa.epis.danp.danp_exam3.HandlerException;

public interface ICameraView extends HandlerException {
    boolean verify_permissions(String[] permissions);
    void request_permissions(String[] permissions, int code);
    Context get_context();
    void start_camera(Preview preview);
    LifecycleOwner get_life_cycle_owner();
    void show_photo(String path);
}
