package com.cgvsu.math;

public class Vector2f {

    public static final float EPS = 1e-7f;

    protected float x, y;

    public static Vector2f getAdded(final Vector2f v1, final Vector2f v2) {
        return new Vector2f(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public static Vector2f getSubtracted(final Vector2f v1, final Vector2f v2) {
        return new Vector2f(v1.getX() - v2.getX(), v1.getY() - v2.getY()) ;
    }

    public static float getScalarProduct(final Vector2f v1, final Vector2f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public static Vector2f getMultipliedVector(final Vector2f v1, final float n) {
        final float x = v1.getX() * n;
        final float y = v1.getY() * n;

        return new Vector2f(x, y);
    }

    public static Vector2f getDividedVector(final Vector2f v1, final float n) throws Exception {
        if (n - 0 < EPS) {
            throw new Exception("На 0 делить нельзя");
        }

        return new Vector2f(v1.getX() / n, v1.getY() / n);
    }

    public static Vector2f getNormalizedVector(final Vector2f v1) {
        if (v1.length() - 0 < EPS) {
            return new Vector2f(0, 0);
        }
        final float invLength = 1 / v1.length();
        final float x = v1.getX() * invLength;
        final float y = v1.getY() * invLength;

        return new Vector2f(x, y);
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setVector(float x, float y) {
        this.x = x;
        this.y = y;
    }

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Vector2f gotVector = (Vector2f) obj;
		return Math.abs(this.x - gotVector.x) < EPS && Math.abs(this.y - gotVector.y) < EPS;
	}

    @Override
    public String toString() {
        return '{' + x + ',' + y + "}";
    }

    public Vector2f copy() {
        return new Vector2f(x, y);
    }

    public float length(){
        return (float) Math.sqrt(x * x + y * y);
    }

    public void add(final Vector2f v2) {
        this.x += v2.getX();
        this.y += v2.getY();
    }

    public void subtract(final Vector2f v2) {
        this.x -= v2.getX();
        this.y -= v2.getY();
    }

    public void multiply(final float n) {
        this.x *= n;
        this.y *= n;
    }

    public void divide(final float n) throws Exception {
        if(n - 0 < EPS) {
            throw new Exception("На 0 делить нельзя");
        }

        this.x = this.x / n;
        this.y = this.y / n;
    }

    public float getScalarProduct(final Vector2f v2) {
        return this.getX() * v2.getX() + this.getY() * v2.getY();
    }

    public void normalize() {
        if (length() - 0 < EPS) {
            this.x = 0;
            this.y = 0;
            return;
        }
        final float invLength = 1 / length();
        this.x *= invLength;
        this.y *= invLength;
    }
}