package com.cgvsu.math;

public class Vector3f {


    public static final float EPS = 1e-7f;
    public float x, y, z;

    public static Vector3f vectorProduct(Vector3f v1, Vector3f v2){
        float x  = v1.getY() * v2.getZ() - v2.getY() * v1.getZ();
        float y  = v1.getX() * v2.getZ() - v2.getX() * v1.getZ();
        float z  = v1.getX() * v2.getY() - v2.getX() * v1.getY();
        return new Vector3f(x, -y, z);
    }

    public static Vector3f sumV(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vector3f difV(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ()) ;
    }

    public static float scalarProduct(Vector3f v1, Vector3f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Vector3f gotVector = (Vector3f) obj;
		return Math.abs(this.x - gotVector.x) < EPS && Math.abs(this.y - gotVector.y) < EPS
				&& Math.abs(this.z - gotVector.z) < EPS;
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return '{' + x + ',' + y + ',' + z + "}";
    }

    public Vector3f copy() {
        return new Vector3f(x, y, z);
    }

    public float length(){
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3f norm() {
        float length = length();
        if (length == 0) {
            return new  Vector3f(0, 0, 0);
        }
        return new Vector3f(x / length, y / length, z / length);
    }
}