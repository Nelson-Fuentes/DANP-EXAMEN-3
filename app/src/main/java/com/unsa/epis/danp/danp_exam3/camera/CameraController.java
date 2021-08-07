package com.unsa.epis.danp.danp_exam3.camera;

import android.content.Context;
import android.os.Environment;
import android.util.Size;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraController {
    public static final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };
    private Executor executor;
    private Context context;
    private CameraSelector camera_selector;
    private Preview preview;
    private ImageAnalysis image_analysis;
    private ProcessCameraProvider camera_provider;
    private ListenableFuture camera_provider_future;
    private Camera camera;
    private ImageCapture image_capture;

    public CameraController(Context context){
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }


    public String[] get_required_permissions(){
        return this.REQUIRED_PERMISSIONS;
    }

    public int get_request_code_permissions(){
        return this.REQUEST_CODE_PERMISSIONS;
    }

    public void start_camera(LifecycleOwner life_cycle_owner) throws ExecutionException, InterruptedException {
        this.build_camera_provider_future();
        this.camera_provider_future.addListener(
                new CameraRunnable(this, life_cycle_owner),
                ContextCompat.getMainExecutor(this.context));
    }

    private void build_preview(){
        this.preview = new Preview.Builder().build();
    }

    private void build_camera_selector(){
        this.camera_selector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
    }
    private void build_image_analysis(){
        this.image_analysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

    }
    private void build_camera(LifecycleOwner life_cycle_owner){
        this.camera = this.camera_provider.bindToLifecycle(
                life_cycle_owner,
                this.camera_selector,
                preview,
                this.image_capture);
    }
    private void build_camera_provider() throws ExecutionException, InterruptedException {
        this.camera_provider = (ProcessCameraProvider) this.camera_provider_future.get();
    }
    private void build_camera_provider_future(){
        this.camera_provider_future = ProcessCameraProvider.getInstance(this.context);
    }

    private void build_image_capturer(){
        ImageCapture.Builder builder = new ImageCapture.Builder();
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);
        //if (hdrImageCaptureExtender.isExtensionAvailable(this.camera_selector)) {
        //    hdrImageCaptureExtender.enableExtension(this.camera_selector);
        //}
        this.image_capture = builder
 //               .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .build();
    }

    public void build(LifecycleOwner life_cycle_owner) throws ExecutionException, InterruptedException {
        this.build_camera_provider();
        this.build_preview();
        this.build_camera_selector();
        this.build_image_analysis();
        this.build_image_capturer();
        this.build_camera(life_cycle_owner);
    }
    public Preview get_preview(){
        return this.preview;
    }

    private File build_file(){
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        File file = new File(this.get_batch_directory_name(), mDateFormat.format(new Date())+ ".jpg");
        return file;
    }

    private String get_batch_directory_name() {
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {
        }
        return app_folder_path;
    }

    public void take_picture(ImageSavedCallback image_saved_callback){
        File file = this.build_file();
        image_saved_callback.set_absolute_path(file.getAbsolutePath());
        ImageCapture.OutputFileOptions output_file_options = new ImageCapture.OutputFileOptions.Builder(file).build();
        this.image_capture.takePicture(output_file_options, this.executor, image_saved_callback);
    }

}
