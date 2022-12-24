package com.cgvsu.math;

public interface Vector {

    public void setVector(float k1, float k2);

    public String getStringV();

    public static Vector sumV(Vector v1, Vector v2) {
        return null;
    }

    public static Vector difV(Vector v1, Vector v2) {
        return null;
    }

    public float length();

    public Vector norm();

    public static float scalarProduct(Vector v1, Vector v2) {
        return 0;
    }

    public static Vector vectorProduct(Vector v1, Vector v2) {
        return null;
    }


}
