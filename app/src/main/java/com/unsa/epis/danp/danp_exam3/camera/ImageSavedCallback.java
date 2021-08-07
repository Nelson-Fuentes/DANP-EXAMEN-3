package com.unsa.epis.danp.danp_exam3.camera;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;

import com.unsa.epis.danp.danp_exam3.HandlerException;

public class ImageSavedCallback implements ImageCapture.OnImageSavedCallback{
    private HandlerException handler;
    private String absolute_path;
    private ICameraView camera_view;

    public ImageSavedCallback(HandlerException handler, ICameraView camera_view){
        this.handler = handler;
        this.camera_view = camera_view;
    }

    public void set_absolute_path(String absolute_path){
        this.absolute_path = absolute_path;
    }

    @Override
    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
        this.camera_view.show_photo(this.absolute_path);
    }

    @Override
    public void onError(@NonNull ImageCaptureException exception) {
        this.handler.handleException(exception);
    }
}
