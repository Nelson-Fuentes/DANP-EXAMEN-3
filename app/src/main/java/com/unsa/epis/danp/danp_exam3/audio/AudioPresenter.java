package com.unsa.epis.danp.danp_exam3.audio;

public class AudioPresenter implements IAudioPresenter{
    private AudioController audio_controller;
    private IAudioView audio_view;
    private OnSoundDetected on_sound_detected;

    public AudioPresenter(IAudioView audio_view, OnSoundDetected on_sound_detected){
        this.audio_controller = new AudioController();
        this.audio_view = audio_view;
        this.on_sound_detected = on_sound_detected;
    }
    @Override
    public void initialize() {
        if (this.verify_permissions()){
            try {
                this.audio_controller.initialize(this.on_sound_detected);
                this.audio_view.listening_initialized();
            } catch (Exception e){
                this.audio_view.handleException(e);
            }
        } else {
            this.request_permissions();
        }
    }

    @Override
    public void turn_off_detect_clap() {
        this.audio_controller.turn_off_detect_clap();
        this.audio_view.turn_off_detect_clap();
    }

    @Override
    public void turn_on_detect_clap() {
        this.audio_controller.turn_on_detect_clap();
        this.audio_view.turn_on_detect_clap();
    }

    @Override
    public void turn_on_detect_whistle() {
        this.audio_controller.turn_on_detect_whistle();
        this.audio_view.turn_on_detect_whistle();
    }

    @Override
    public void turn_off_detect_whistle() {
        this.audio_controller.turn_off_detect_whistle();
        this.audio_view.turn_off_detect_whistle();
    }

    private boolean verify_permissions(){
        return this.audio_view.verify_permissions(this.audio_controller.get_required_permissions());
    }

    private void request_permissions(){
        this.audio_view.request_permissions(
                this.audio_controller.get_required_permissions(),
                this.audio_controller.get_request_code_permissions()
        );
    }
}
