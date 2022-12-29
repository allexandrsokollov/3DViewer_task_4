package com.cgvsu.math;

public class Vector4f {

    public static final float EPS = 1e-7f;
    public float x, y, z, w;

    public static Vector4f normalization(final Vector4f v1) {
        if (v1.length() - 0 < EPS) {
            return new Vector4f(0, 0, 0, 0);
        }
        final float invLength = 1 / v1.length();
        final float x = v1.getX() * invLength;
        final float y = v1.getY() * invLength;
        final float z = v1.getZ() * invLength;
        final float w = v1.getW() * invLength;

        return new Vector4f(x, y, z, w);
    }

    public static Vector4f sum(final Vector4f v1, final Vector4f v2) {
        return new Vector4f(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ(),
                v1.getW() + v2.getW());
    }

    public static Vector4f sub(final Vector4f v1, final Vector4f v2) {
        return new Vector4f(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ(),
                v1.getW() - v2.getW());
    }

    public static Vector4f multiply(final Vector4f v1, final float n) {
        final float x = v1.getX() * n;
        final float y = v1.getY() * n;
        final float z = v1.getZ() * n;
        final float w = v1.getW() * n;

        return new Vector4f(x, y, z, w);
    }

    public static Vector4f divide(final Vector4f v1, final float n) throws Exception {
        if(n - 0 < EPS) {
            throw new Exception("На 0 делить нельзя");
        }
        final float x = v1.getX() / n;
        final float y = v1.getY() / n;
        final float z = v1.getZ() / n;
        final float w = v1.getW() / n;

        return new Vector4f(x, y, z, w);
    }

    public static float scalarProduct(final Vector4f v1, final Vector4f v2) {
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
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Vector4f gotVector = (Vector4f) obj;
        return Math.abs(this.x - gotVector.x) < EPS && Math.abs(this.y - gotVector.y) < EPS
                && Math.abs(this.z - gotVector.z) < EPS && Math.abs(this.w - gotVector.w) < EPS;
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

    public void normalization() {
        if (length() - 0 < EPS) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.w = 0;
            return;
        }
        final float invLength = 1 / length();
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
        this.w *= invLength;
    }

    public void sum(final Vector4f v2) {
        this.x += v2.getX();
        this.y += v2.getY();
        this.z += v2.getZ();
        this.w += v2.getW();
    }

    public void sub(final Vector4f v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
        this.z -= v2.getZ();
        this.w -= v2.getW();
    }

    public void multiply(final float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        this.w *= n;
    }

    public void divide(final float n) throws Exception {
        if(n - 0 < EPS) {
            throw new Exception("На 0 делить нельзя");
        }

        this.x = this.x / n;
        this.y = this.y / n;
        this.z = this.z / n;
        this.w = this.w / n;
    }

    public float scalarProduct(final Vector4f v2) {
        return this.x * v2.getX() + this.y * v2.getY() + this.z * v2.getZ() + this.w * v2.getW();
    }
}
