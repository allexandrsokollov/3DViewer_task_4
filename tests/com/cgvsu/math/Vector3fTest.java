package com.cgvsu.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector3fTest {

    @Test
    public void equalsTest() {
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f2 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f3 = new Vector3f(1.3F, 1.5F, 1.5F);
        Vector3f vector3f4 = new Vector3f(1.5F, 1.3F, 1.5F);

        assertEquals(vector3f1, vector3f1);
        assertEquals(vector3f1, vector3f2);
        assertNotEquals(vector3f3, vector3f1);
        assertNotEquals(vector3f3, vector3f4);
    }

    @Test
    public void copyTest() {
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);

        Vector3f vector3f2 = vector3f1.copy();
        assertEquals(vector3f2, vector3f1);
    }

    @Test
    public void normTest() {
        Vector3f vector3f0 = new Vector3f(0, 0, 0);
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f2 = Vector3f.getNormalizedVector(vector3f1);

        Vector3f vector3f3 = new Vector3f(1.5F / vector3f1.length(), 1.5F / vector3f1.length(), 1.5F / vector3f1.length());

        vector3f1.normalize();
        assertEquals(vector3f3, vector3f2);
        assertEquals(vector3f3, vector3f1);

        Vector3f vector3f = vector3f0.copy();
        Vector3f vector3fn0 = Vector3f.getNormalizedVector(vector3f0);
        vector3f0.normalize();
        assertEquals(vector3fn0, vector3f);
        assertEquals(vector3f0, vector3f);
    }

    @Test
    public void addTest() {
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f2 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f3 = new Vector3f(3.0F, 3.0F, 3.0F);

        Vector3f vector3f = Vector3f.getSummarized(vector3f1, vector3f2);
        vector3f1.add(vector3f2);
        assertEquals(vector3f3, vector3f);
        assertEquals(vector3f3, vector3f1);

    }

    @Test
    public void subTest() {
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f2 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f3 = new Vector3f(0, 0, 0);

        Vector3f vector3f = Vector3f.getAdded(vector3f1, vector3f2);
        vector3f1.subtract(vector3f2);
        assertEquals(vector3f3, vector3f);
        assertEquals(vector3f3, vector3f1);
    }

    @Test
    public void multiplyTest() {
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f = new Vector3f(3, 3, 3);
        float n = 2;

        assertEquals(vector3f, Vector3f.getMultipliedVector(vector3f1, n));
        vector3f1.multiply(n);
        assertEquals(vector3f, vector3f1);
    }

    @Test
    public void  divideTest() throws Exception {
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f = new Vector3f(0.5F, 0.5F, 0.5F);
        final float n = 3.0F;

        assertEquals(vector3f, Vector3f.getDividedVector(vector3f1, n));
        vector3f1.divide(n);
        assertEquals(vector3f, vector3f1);


        Exception exception = assertThrows(Exception.class, () -> {
            Vector3f.getDividedVector(vector3f1, 0);
        });

        String eS = "На 0 делить нельзя";
        String act = exception.getMessage();
        assertTrue(act.contains(eS));
    }

    @Test
    public void scalarProductTest() {
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f2 = new Vector3f(1.5F, 1.5F, 1.5F);

        float sc = 6.75F;

        assertEquals(sc, vector3f1.getScalarProduct(vector3f2));
        assertEquals(sc, Vector3f.getScalarProduct(vector3f1, vector3f2));
    }

    @Test
    public void vectorProductTest() {
        Vector3f vector3f1 = new Vector3f(1.5F, 1.5F, 1.5F);
        Vector3f vector3f2 = new Vector3f(2F, 2F, 2F);

        Vector3f vector3f = new Vector3f(0, 0, 0);
        assertEquals(vector3f, Vector3f.getVectorProduct(vector3f1, vector3f2));
    }


}