package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import javafx.fxml.FXML;

public class CameraController {

    private Vector3f forwardV;
    private Vector3f backwardV;
    private Vector3f leftV;
    private Vector3f rightV;
    private Vector3f upV;
    private Vector3f downV;
    private float translation;

    private Camera camera;


    public CameraController(Camera camera, float translation) {
        this.translation = translation;
        this.camera = camera;
        forwardV = new Vector3f(0, 0, -translation);
        backwardV = new Vector3f(0, 0, translation);
        leftV = new Vector3f(translation, 0, 0);
        rightV = new Vector3f(-translation, 0, 0);
        upV = new Vector3f(0, translation, 0);
        downV = new Vector3f(0, -translation, 0);
    }

    public void handleCameraForward() throws Exception {
        camera.moveCamera(forwardV);
    }

    public void handleCameraBackward() throws Exception {
        camera.moveCamera(backwardV);
    }

    public void handleCameraLeft() throws Exception {
        camera.moveCamera(leftV);
    }


    public void handleCameraRight() throws Exception {
        camera.moveCamera(rightV);
    }


    public void handleCameraUp() throws Exception {
        camera.moveCamera(upV);
    }


    public void handleCameraDown() throws Exception {
        camera.moveCamera(downV);
    }

    public void rotateCamera(final Vector2f angleOfRotate) throws Exception {
        Matrix4 mR = GraphicConveyor.getRotationMatrix(new Vector3f(angleOfRotate.getY(), angleOfRotate.getX(),0));
        camera.rotateCamera(mR);
        forwardV = GraphicConveyor.multiplyMatrix4ByVector3(mR, forwardV);
        backwardV = GraphicConveyor.multiplyMatrix4ByVector3(mR, backwardV);
        leftV = GraphicConveyor.multiplyMatrix4ByVector3(mR, leftV);
        rightV = GraphicConveyor.multiplyMatrix4ByVector3(mR, rightV);
    }



}