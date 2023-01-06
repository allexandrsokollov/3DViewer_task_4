package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix3;
import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2f;
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

    public void rotateCamera(final Vector2f angleOfRotate) throws Exception {
        double radiantA = Math.toRadians(angleOfRotate.getX());
        double radiantB = Math.toRadians(angleOfRotate.getY());

        float cosA = (float) Math.cos(radiantA);
        float cosB = (float) Math.cos(radiantB);
        float sinA = (float) Math.sin(radiantA);
        float sinB = (float) Math.sin(radiantB);

        Matrix4 matrixRotate = Matrix4.getIdentityMatrix();

        /*Matrix4 matrixA = new Matrix4();
        matrixA.setIdentity();
        matrixA.getMatrix()[0][0] = cosA;
        matrixA.getMatrix()[1][1] = cosA;
        matrixA.getMatrix()[1][0] = sinA;
        matrixA.getMatrix()[0][1] = -sinA;

        Matrix4 matrixB = new Matrix4();
        matrixB.setIdentity();
        matrixB.getMatrix()[0][0] = cosB;
        matrixB.getMatrix()[1][1] = cosB;
        matrixB.getMatrix()[1][0] = sinB;
        matrixB.getMatrix()[0][1] = -sinB;

        matrixRotate.multiply(matrixB);
        matrixRotate.multiply(matrixA);*/

        float x = cosA * cosB;
        float y = sinB;
        float z = cosB * sinA;

        Vector3f rotate = new Vector3f(x, y, z);

        //matrixRotate.transpose();
        /*position.setX((float) (Math.cos(radiantA) * Math.cos(radiantB)));
        position.setY((float) Math.sin(radiantB));
        position.setZ((float) (Math.sin(radiantA) * Math.cos(radiantB)));*/
        //Vector3f tmp = Vector3f.getSubtracted(target, position);
        //tmp = Matrix3.getMultiplied(matrixRotate, tmp);
        //target = Vector3f.getAdded(position, tmp);
        //target = GraphicConveyor.multiplyMatrix4ByVector3(matrixRotate, target);
        target = rotate;
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