package com.cgvsu.math;

public class Vector2f {

    public static final float EPS = 1e-7f;

    protected float x, y;

    public static Vector2f sumV(Vector2f v1, Vector2f v2) {
        return new Vector2f(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public static Vector2f difV(Vector2f v1, Vector2f v2) {
        return new Vector2f(v1.getX() - v2.getX(), v1.getY() - v2.getY()) ;
    }

    public static float scalarProduct(Vector2f v1, Vector2f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
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

    public Vector2f norm() {
        float length = length();
        if (length == 0) {
            return new  Vector2f(0, 0);
        }
        return new Vector2f(x / length, y / length);
    }
}