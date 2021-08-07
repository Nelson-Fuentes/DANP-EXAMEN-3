package com.unsa.epis.danp.danp_exam3.audio;

public class AudioController {
    public static final int REQUEST_CODE_PERMISSIONS = 102;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.RECORD_AUDIO",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };


    private RecorderThread recorder_thread;
    private DetectorThread detector_thread;

    public AudioController(){}

    public void initialize(OnSoundDetected sound_detected){
        this.recorder_thread = new RecorderThread();
        this.detector_thread = new DetectorThread(recorder_thread, sound_detected);
        this.start_listening();
    }

    public void start_listening(){
        this.recorder_thread.start();
        this.detector_thread.start();
    }

    public String[] get_required_permissions(){
        return this.REQUIRED_PERMISSIONS;
    }

    public int get_request_code_permissions(){
        return this.REQUEST_CODE_PERMISSIONS;
    }

    public void turn_on_detect_clap(){
        this.detector_thread.set_detect_clap(true);
    }

    public void turn_off_detect_clap(){
        this.detector_thread.set_detect_clap(false);
    }

    public void turn_on_detect_whistle(){
        this.detector_thread.set_detect_whistle(true);
    }

    public void turn_off_detect_whistle(){
        this.detector_thread.set_detect_whistle(false);
    }

}
