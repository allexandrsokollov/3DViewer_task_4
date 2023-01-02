package com.cgvsu.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector4fTest {
    @Test
    public void equalsTest() {
        Vector4f vector4f1 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f2 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f3 = new Vector4f(1.3F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f4 = new Vector4f(1.5F, 1.3F, 1.5F, 1.5F);

        assertEquals(vector4f1, vector4f1);
        assertEquals(vector4f1, vector4f2);
        assertNotEquals(vector4f3, vector4f1);
        assertNotEquals(vector4f3, vector4f4);
    }

    @Test
    public void copyTest() {
        Vector4f vector4f1 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);

        Vector4f vector4f2 = vector4f1.copy();
        assertEquals(vector4f2, vector4f1);
    }

    @Test
    public void normTest() {
        Vector4f vector4f0 = new Vector4f(0, 0, 0, 0);
        Vector4f vector4f1 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f2 = Vector4f.getNormalizedVector(vector4f1);

        Vector4f vector4f3 = new Vector4f(1.5F / vector4f1.length(), 1.5F / vector4f1.length(), 1.5F / vector4f1.length(),
        1.5F/ vector4f1.length());

        vector4f1.normalize();
        assertEquals(vector4f3, vector4f2);
        assertEquals(vector4f3, vector4f1);

        Vector4f vector4f = vector4f0.copy();
        Vector4f vector4fn0 = Vector4f.getNormalizedVector(vector4f0);
        vector4f0.normalize();
        assertEquals(vector4fn0, vector4f);
        assertEquals(vector4f0, vector4f);
    }

    @Test
    public void addTest() {
        Vector4f vector4f1 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f2 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f3 = new Vector4f(3.0F, 3.0F, 3.0F, 3.0F);

        Vector4f vector4f = Vector4f.getAdded(vector4f1, vector4f2);
        vector4f1.add(vector4f2);
        assertEquals(vector4f3, vector4f);
        assertEquals(vector4f3, vector4f1);
    }

    @Test
    public void subTest() {
        Vector4f vector4f1 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f2 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f3 = new Vector4f(0, 0, 0, 0);

        Vector4f vector4f = Vector4f.getSubtracted(vector4f1, vector4f2);
        vector4f1.subtract(vector4f2);
        assertEquals(vector4f3, vector4f);
        assertEquals(vector4f3, vector4f1);
    }

    @Test
    public void multiplyTest() {
        Vector4f vector4f1 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f = new Vector4f(3, 3, 3, 3);
        float n = 2;

        assertEquals(vector4f, Vector4f.getMultipliedVector(vector4f1, n));
        vector4f1.multiply(n);
        assertEquals(vector4f, vector4f1);
    }

    @Test
    public void  divideTest() throws Exception {
        Vector4f vector4f1 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f = new Vector4f(0.5F, 0.5F, 0.5F, 0.5F);
        final float n = 3.0F;

        assertEquals(vector4f, Vector4f.getDividedVector(vector4f1, n));
        vector4f1.divide(n);
        assertEquals(vector4f, vector4f1);


        Exception exception = assertThrows(Exception.class, () -> {
            Vector4f.getDividedVector(vector4f1, 0);
        });

        String eS = "На 0 делить нельзя";
        String act = exception.getMessage();
        assertTrue(act.contains(eS));
    }

    @Test
    public void scalarProductTest() {
        Vector4f vector4f1 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);
        Vector4f vector4f2 = new Vector4f(1.5F, 1.5F, 1.5F, 1.5F);

        float sc = 9F;

        assertEquals(sc, vector4f1.getScalarProduct(vector4f2));
        assertEquals(sc, Vector4f.getScalarProduct(vector4f1, vector4f2));
    }
}