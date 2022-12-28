package com.cgvsu.math;

public class Vector4f {

    public static final float EPS = 1e-7f;
    public float x, y, z, w;

    public static Vector4f sumV(Vector4f v1, Vector4f v2) {
        return new Vector4f(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ(),
                v1.getW() + v2.getW());
    }

    public static Vector4f difV(Vector4f v1, Vector4f v2) {
        return new Vector4f(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ(),
                v1.getW() - v2.getW());
    }

    public static float scalarProduct(Vector4f v1, Vector4f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ() +
                v1.getW() * v2.getW();
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void setVector(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Override
    public String toString() {
        return '{' + x + ',' + y + ',' + z + ',' + w + "}";
    }

    public Vector4f copy() {
        return new Vector4f(x, y, z, w);
    }

    public float length(){
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Vector4f norm() {
        float length = length();
        if (length == 0) {
            return new  Vector4f(0, 0, 0, 0);
        }
        return new Vector4f(x / length, y / length, z / length, w / length);
    }
}
