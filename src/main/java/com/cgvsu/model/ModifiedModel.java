package com.cgvsu.model;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.render_engine.GraphicConveyor;

import java.util.ArrayList;
import java.util.List;

public class ModifiedModel extends Model {
    private Vector3f rotateV;
    private Vector3f scaleV;
    private Vector3f translateV;

    public ModifiedModel(Model model) {
        super(model);
        translateV = new Vector3f(0, 0, 0);
        rotateV = new Vector3f(0, 0, 0);
        scaleV = new Vector3f(1, 1, 1);
    }

    public ModifiedModel(ModifiedModel model) {
        super(model);
        translateV = model.getTranslateV();
        rotateV = model.getRotateV();
        scaleV = model.getScaleV();
    }

    public ModifiedModel(Model model, Vector3f translateV, Vector3f rotateV, Vector3f scaleV){
        super(model);
        this.translateV = translateV;
       this.rotateV =  rotateV;
       this.scaleV = scaleV;
    }

    public Matrix4 getModelMatrix() throws Exception {
        Matrix4 modelMatrix = GraphicConveyor.getModelMatrix(translateV, rotateV, scaleV);
        modelMatrix.transpose();
        return modelMatrix;
    }

    public ArrayList<Vector3f> getModifiedVertexes () throws Exception {
        ArrayList<Vector3f> newVertexes = new ArrayList<>();
        Matrix4 modelMatrix = getModelMatrix();

        for (int i = 0; i < super.getVertices().size(); i++) {
            Vector3f vertex = new Vector3f(super.getVertices().get(i).getX(),super.getVertices().get(i).getY(), super.getVertices().get(i).getZ());
            Vector3f multipliedVector = GraphicConveyor.multiplyMatrix4ByVector3(modelMatrix, vertex);
            newVertexes.add(multipliedVector);
        }
        return newVertexes;
    }

    public Model getTransformedModel () throws Exception {
        List<Vector2f> tV = List.copyOf(super.getTextureVertices());
        List<Polygon> p = List.copyOf(super.getPolygons());
        List<Vector3f> nV = List.copyOf(super.getNormals());
        List<Vector3f> vM = getModifiedVertexes();
        Model transformedModel = new Model(vM, tV, nV, p);
        //transformedModel.recalculateNormals();
        return transformedModel;
    }

    public Model toMesh() {
        try {
            return new Model(super.getVertices(), super.getTextureVertices(), super.getNormals(), super.getPolygons());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Vector3f getRotateV() {
        return rotateV;
    }

    public void setRotateV(Vector3f rotateV) {
        this.rotateV = rotateV;
    }

    public Vector3f getScaleV() {
        return scaleV;
    }

    public void setScaleV(Vector3f scaleV) {
        this.scaleV = scaleV;
    }

    public Vector3f getTranslateV() {
        return translateV;
    }

    public void setTranslateV(Vector3f translateV) {
        this.translateV = translateV;
    }
}