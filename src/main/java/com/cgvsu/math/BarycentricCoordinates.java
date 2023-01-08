package com.cgvsu.math;

public class BarycentricCoordinates {
    float u;
    float v;
    float w;

    public BarycentricCoordinates(float x, float y, float x1, float x2, float x3, float y1, float y2, float y3){
        float triangleArea = (y1 - y3) * (x2 - x3) + (y2 - y3) * (x3 - x1);
        this.u = ((y - y3) * (x2 - x3) + (y2 - y3) * (x3 - x)) / triangleArea;
        this.v = ((y - y1) * (x3 - x1) + (y3 - y1) * (x1 - x)) / triangleArea;
        this.w = ((y - y2) * (x1 - x2) + (y1 - y2) * (x2 - x)) / triangleArea;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }


    public float getW() {
        return w;
    }

}
