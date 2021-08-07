package com.unsa.epis.danp.danp_exam3.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.snackbar.Snackbar;
import com.unsa.epis.danp.danp_exam3.R;
import com.unsa.epis.danp.danp_exam3.audio.AudioController;
import com.unsa.epis.danp.danp_exam3.audio.AudioPresenter;
import com.unsa.epis.danp.danp_exam3.audio.IAudioPresenter;
import com.unsa.epis.danp.danp_exam3.audio.IAudioView;
import com.unsa.epis.danp.danp_exam3.audio.OnSoundDetected;

public class CameraActivity extends AppCompatActivity implements ICameraView, IAudioView, OnSoundDetected {
    private PreviewView preview_view;
    private ICameraPresenter camera_presenter;
    private IAudioPresenter audio_presenter;
    private Button take_picture_button;
    private MaterialButtonToggleGroup clap_button;
    private MaterialButtonToggleGroup whistle_button;
    public static final int REQUEST_CODE_PERMISSIONS = 103;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        this.preview_view = findViewById(R.id.previewView);
        this.initialize();
    }

    public void initialize(){
        if (this.verify_permissions(this.REQUIRED_PERMISSIONS)){
            this.camera_presenter = new CameraPresenter(this);
            this.camera_presenter.initialize();
            this.take_picture_button = (Button) this.findViewById(R.id.take_picture_button);
            this.take_picture_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    camera_presenter.take_picture();
                }
            });
            this.clap_button = (MaterialButtonToggleGroup) this.findViewById(R.id.clap_toggle);
            this.clap_button.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                    if (isChecked){
                        audio_presenter.turn_on_detect_clap();
                    } else {
                        audio_presenter.turn_off_detect_clap();
                    }
                }
            });

            this.whistle_button = (MaterialButtonToggleGroup) this.findViewById(R.id.whistle_toggle);
            this.whistle_button.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                    if (isChecked){
                        audio_presenter.turn_on_detect_whistle();
                    } else {
                        audio_presenter.turn_off_detect_whistle();
                    }
                }
            });
            this.audio_presenter = new AudioPresenter(this ,this);
            this.audio_presenter.initialize();
        } else {
            this.request_permissions(this.REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AudioController.REQUEST_CODE_PERMISSIONS){
            this.audio_presenter.initialize();
        }
        if (requestCode == CameraController.REQUEST_CODE_PERMISSIONS){
            this.camera_presenter.initialize();
        }
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            this.initialize();
        }

    }

    @Override
    public boolean verify_permissions(String[] permissions) {
        for(String permission : permissions){

            if(ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void request_permissions(String[] permissions, int code) {
        ActivityCompat.requestPermissions(this, permissions, code);

    }

    @Override
    public Context get_context() {
        return this;
    }

    @Override
    public void listening_initialized() {
        Snackbar.make(this.preview_view, "Se inicio a escuchar", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void turn_on_detect_clap() {
        Snackbar.make(this.preview_view, "Se inicio la detección de aplausos", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void turn_off_detect_clap() {
        Snackbar.make(this.preview_view, "Se apago la detección de aplausos", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void turn_on_detect_whistle() {
        Snackbar.make(this.preview_view, "Se inicio la detección de silbidos", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void turn_off_detect_whistle() {
        Snackbar.make(this.preview_view, "Se apago la detección de silbidos", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void start_camera(Preview preview) {
        preview.setSurfaceProvider(this.preview_view.getSurfaceProvider());

    }

    @Override
    public LifecycleOwner get_life_cycle_owner() {
        return (LifecycleOwner) this;
    }

    @Override
    public void show_photo(String path) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ViewGroup view_group = findViewById(android.R.id.content);
                View dialog_view = LayoutInflater.from(get_context()).inflate(R.layout.photo_dialog, view_group, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(get_context());
                builder.setView(dialog_view);
                AlertDialog alert_dialog = builder.create();

                Bitmap bitmap = BitmapFactory.decodeFile(path);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                ImageView photo_image_view = (ImageView) dialog_view.findViewById(R.id.photo);
                photo_image_view.setImageBitmap(bitmap);

                alert_dialog.show();
            }
        });

    }

    @Override
    public void handleException(Exception e) {
        Snackbar.make(this.preview_view, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
        e.getStackTrace();
        System.out.println(e);
    }

    @Override
    public void sound_detected() {
        this.camera_presenter.take_picture();
    }
}