package com.cgvsu.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class Vector2fTest {
     @Test
    public void equalsTest() {
         Vector2f vector2f1 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f2 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f3 = new Vector2f(1.3F, 1.5F);
         Vector2f vector2f4 = new Vector2f(1.5F, 1.3F);

         assertEquals(vector2f1, vector2f1);
         assertEquals(vector2f1, vector2f2);
         assertNotEquals(vector2f3, vector2f1);
         assertNotEquals(vector2f3, vector2f4);
     }

     @Test
    public void copyTest() {
         Vector2f vector2f1 = new Vector2f(1.5F, 1.5F);

         Vector2f vector2f2 = vector2f1.copy();
         assertEquals(vector2f2, vector2f1);
     }

     @Test
     public void normTest() {
         Vector2f vector2f0 = new Vector2f(0, 0);
         Vector2f vector2f1 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f2 = Vector2f.getNormalizedVector(vector2f1);

         Vector2f vector2f3 = new Vector2f(1.5F / vector2f1.length(), 1.5F / vector2f1.length());

         vector2f1.normalize();
         assertEquals(vector2f3, vector2f2);
         assertEquals(vector2f3, vector2f1);

         Vector2f vector2f = vector2f0.copy();
         Vector2f vector2fn0 = Vector2f.getNormalizedVector(vector2f0);
         vector2f0.normalize();
         assertEquals(vector2fn0, vector2f);
         assertEquals(vector2f0, vector2f);
     }

     @Test
    public void sumTest() {
         Vector2f vector2f1 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f2 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f3 = new Vector2f(3.0F, 3.0F);

         Vector2f vector2f = Vector2f.getSummarized(vector2f1, vector2f2);
         vector2f1.summarize(vector2f2);
         assertEquals(vector2f3, vector2f);
         assertEquals(vector2f3, vector2f1);

     }

     @Test
     public void subTest() {
         Vector2f vector2f1 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f2 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f3 = new Vector2f(0, 0);

         Vector2f vector2f = Vector2f.getSubtracted(vector2f1, vector2f2);
         vector2f1.subtract(vector2f2);
         assertEquals(vector2f3, vector2f);
         assertEquals(vector2f3, vector2f1);
     }

     @Test
     public void multiplyTest() {
         Vector2f vector2f1 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f = new Vector2f(3, 3);
         float n = 2;

         assertEquals(vector2f, Vector2f.getMultiplyVector(vector2f1, n));
         vector2f1.multiply(n);
         assertEquals(vector2f, vector2f1);
     }

     @Test
     public void  divideTest() throws Exception {
         Vector2f vector2f1 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f = new Vector2f(0.5F, 0.5F);
         float n = 3.0F;

         assertEquals(vector2f, Vector2f.getDividedVector(vector2f1, n));
         vector2f1.divide(n);
         assertEquals(vector2f, vector2f1);


         Exception exception = assertThrows(Exception.class, () -> {
             Vector2f.getDividedVector(vector2f1, 0);
         });

         String eS = "На 0 делить нельзя";
         String act = exception.getMessage();
         assertTrue(act.contains(eS));
     }

     @Test
    public void scalarProductTest() {
         Vector2f vector2f1 = new Vector2f(1.5F, 1.5F);
         Vector2f vector2f2 = new Vector2f(1.5F, 1.5F);

         float sc = 4.5F;

         assertEquals(sc, vector2f1.getScalarProduct(vector2f2));
         assertEquals(sc, Vector2f.getScalarProduct(vector2f1, vector2f2));
     }

}