package com.cgvsu.math;

public class Vector3f {


    public static final float EPS = 1e-7f;
    private float x, y, z;

    public static Vector3f getVectorProduct(final Vector3f v1, final Vector3f v2) {
        final float x  = v1.getY() * v2.getZ() - v2.getY() * v1.getZ();
        final float y  = v2.getX() * v1.getZ() - v2.getZ() * v1.getX();
        final float z  = v1.getX() * v2.getY() - v2.getX() * v1.getY();
        return new Vector3f(x, y, z);
    }

    public static Vector3f getAdded(final Vector3f v1, final Vector3f v2) {
        return new Vector3f(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vector3f getSubtracted(final Vector3f v1, final Vector3f v2) {
        return new Vector3f(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ()) ;
    }

    public static Vector3f getMultipliedVector(final Vector3f v1, final float n) {
        final float x = v1.getX() * n;
        final float y = v1.getY() * n;
        final float z = v1.getZ() * n;

        return new Vector3f(x, y, z);
    }

    public static Vector3f getDividedVector(final Vector3f v1, final float n) throws Exception {
        if (n - 0 < EPS) {
            throw new Exception("На 0 делить нельзя");
        }
        final float x = v1.getX() / n;
        final float y = v1.getY() / n;
        final float z = v1.getZ() / n;

        return new Vector3f(x, y, z);
    }

    public static float getScalarProduct(final Vector3f v1, final Vector3f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    public static Vector3f getNormalizedVector(final Vector3f v1) {
        if (v1.length() - 0 < EPS) {
            return new Vector3f(0, 0, 0);
        }
        final float invLength = 1 / v1.length();
        final float x = v1.getX() * invLength;
        final float y = v1.getY() * invLength;
        final float z = v1.getZ() * invLength;

        return new Vector3f(x, y, z);
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

    public void normalize() {
        if (length() - 0 < EPS) {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            return;
        }
        final float invLength = 1 / length();
        this.x *= invLength;
        this.y *= invLength;
        this.z *= invLength;
    }

    public void add(final Vector3f v2) {
        this.x += v2.getX();
        this.y += v2.getY();
        this.z += v2.getZ();
    }

    public void subtract(final Vector3f v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
        this.z -= v2.getZ();
    }

    public void multiply(final float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }

    public void divide(final float n) throws Exception{
        if (n - 0 < EPS) {
            throw new Exception("На 0 делить нельзя");
        }

        this.x = this.x / n;
        this.y = this.y / n;
        this.z = this.z / n;
    }

    public float getScalarProduct(final Vector3f v2) {
        return this.getX() * v2.getX() + this.getY() * v2.getY() + this.getZ() * v2.getZ();
    }

}