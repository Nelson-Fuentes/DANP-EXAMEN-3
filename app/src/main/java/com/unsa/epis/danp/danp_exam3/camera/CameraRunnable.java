package com.unsa.epis.danp.danp_exam3.camera;

import androidx.lifecycle.LifecycleOwner;

import java.util.concurrent.ExecutionException;

public class CameraRunnable implements Runnable{
    private CameraController camera_controller;

    public CameraRunnable(CameraController camera_controller, LifecycleOwner life_cycle_owner) throws ExecutionException, InterruptedException {
        this.camera_controller = camera_controller;
        this.camera_controller.build(life_cycle_owner);
    }

    @Override
    public void run() {
    }
}
