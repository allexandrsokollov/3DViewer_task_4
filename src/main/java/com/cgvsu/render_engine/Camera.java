package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3f;



public class Camera {

    public Camera(
            final Vector3f position,
            final Vector3f target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public void setTarget(final Vector3f target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getTarget() {
        return target;
    }

    public void moveCamera(final Vector3f translation) throws Exception {
        /*Matrix4 modelMatrix = GraphicConveyor.getModelMatrix(translation, new Vector3f(0,0,0), new Vector3f(1,1,1));
        modelMatrix.transpose();*/

       movePosition(translation);
       moveTarget(translation);
    }

    public void movePosition(final Vector3f translation) throws Exception {
        this.position.add(translation);
    }

    public void moveTarget(final Vector3f translation) {
        this.target.add(translation);
    }

    public void rotateCamera(final Vector3f rotation) {

    }

    Matrix4 getViewMatrix() throws Exception {
        return GraphicConveyor.lookAt(position, target);
    }

    Matrix4 getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    private Vector3f position;
    private Vector3f target;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
}