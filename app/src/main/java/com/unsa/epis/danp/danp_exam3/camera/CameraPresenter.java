package com.unsa.epis.danp.danp_exam3.camera;

public class CameraPresenter implements ICameraPresenter{
    private ICameraView camera_view;
    private CameraController camera_controller;

    public CameraPresenter(ICameraView camera_view){
        this.camera_view = camera_view;
        this.camera_controller = new CameraController(this.camera_view.get_context());
    }

    @Override
    public void initialize() {
        if (this.verify_permissions()){
            try {
                this.camera_controller.start_camera(this.camera_view.get_life_cycle_owner());
                this.camera_view.start_camera(this.camera_controller.get_preview());
            } catch (Exception e){
                this.camera_view.handleException(e);
            }

        } else {
            this.request_permissions();
        }
    }

    @Override
    public void take_picture() {
        this.camera_controller.take_picture(new ImageSavedCallback(this.camera_view, this.camera_view));
    }

    private boolean verify_permissions(){
        return this.camera_view.verify_permissions(this.camera_controller.get_required_permissions());
    }

    private void request_permissions(){
        this.camera_view.request_permissions(
                this.camera_controller.get_required_permissions(),
                this.camera_controller.get_request_code_permissions()
        );
    }
}
