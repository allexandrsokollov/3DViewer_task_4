package com.cgvsu.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix3Test {
    @Test
    public void sumTest() throws Exception {

        Matrix3 m1 = new Matrix3( new float[][] {
                        {1,1,1},
                        {2,2,2},
                        {3,3,3}
                });
        Matrix3 m2 = new Matrix3( new float[][] {
                {3,3,3},
                {2,2,2},
                {1,1,1}
        });
        Matrix3 m3 = new Matrix3( new float[][] {
                {4,4,4},
                {4,4,4},
                {4,4,4}
        });
        Matrix3 m0 = new Matrix3( new float[][] {
                {0,0,0},
                {0,0,0},
                {0,0,0}
        });
        Matrix3 mR = Matrix3.getSummarized(m1, m2);
        m1.summarize(m2);
        Matrix3 mR0 = Matrix3.getSummarized(m1, m0);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(m3.getMatrix()[row][col], mR.getMatrix()[row][col]);
                assertEquals(m3.getMatrix()[row][col], m1.getMatrix()[row][col]);
                assertEquals(m1.getMatrix()[row][col], mR0.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void subTest() throws Exception {
        Matrix3 m1 = new Matrix3( new float[][] {
                {1,1,1},
                {2,2,2},
                {3,3,3}
        });
        Matrix3 m2 = new Matrix3( new float[][] {
                {3,3,3},
                {2,2,2},
                {1,1,1}
        });
        Matrix3 m3 = new Matrix3( new float[][] {
                {-2,-2,-2},
                {0,0,0},
                {2,2,2}
        });
        Matrix3 m0 = new Matrix3( new float[][] {
                {0,0,0},
                {0,0,0},
                {0,0,0}
        });
        Matrix3 mR = Matrix3.getSubtracted(m1, m2);
        m1.subtract(m2);
        Matrix3 mR0 = Matrix3.getSubtracted(m1, m0);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(m3.getMatrix()[row][col], mR.getMatrix()[row][col]);
                assertEquals(m3.getMatrix()[row][col], m1.getMatrix()[row][col]);
                assertEquals(m1.getMatrix()[row][col], mR0.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void multiplyVTest() throws Exception {
        Matrix3 m1 = new Matrix3( new float[][] {
                {1,1,1},
                {2,2,2},
                {3,3,3}
        });
        Vector3f v1 = new Vector3f(1, 2, 3);
        Vector3f vR = new Vector3f(6, 12, 18);
        Vector3f v = Matrix3.getMultiply(m1, v1);
        assertEquals(vR, v);
        Vector3f v2 = m1.getMultiply(v1);
        assertEquals(vR, v2);

    }

    @Test
    public void multiplyMTest() throws Exception {
        Matrix3 m1 = new Matrix3( new float[][] {
                {1,1,1},
                {2,2,2},
                {3,3,3}
        });
        Matrix3 m2 = new Matrix3( new float[][] {
                {1,1,1},
                {2,2,2},
                {3,3,3}
        });

        Matrix3 m3 = Matrix3.getMultiply(m1, m2);

        Matrix3 mR = new Matrix3( new float[][] {
                {6,6,6},
                {12,12,12},
                {18,18,18}
        });
        m1.multiply(m2);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(mR.getMatrix()[row][col], m1.getMatrix()[row][col]);
                assertEquals(mR.getMatrix()[row][col], m3.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void multiplyNTest() throws Exception {
        Matrix3 m1 = new Matrix3( new float[][] {
                {1,1,1},
                {2,2,2},
                {3,3,3}
        });
        float n = 3;

        Matrix3 m3 = Matrix3.getMultiply(m1, n);

        Matrix3 mR = new Matrix3( new float[][] {
                {3,3,3},
                {6,6,6},
                {9,9,9}
        });
        m1.multiply(n);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(mR.getMatrix()[row][col], m1.getMatrix()[row][col]);
                assertEquals(mR.getMatrix()[row][col], m3.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void transMatrixTest() throws Exception {
        Matrix3 m1 = new Matrix3( new float[][] {
                {1,1,1},
                {2,2,2},
                {3,3,3}
        });
        Matrix3 m2 = new Matrix3( new float[][] {
                {1,2,3},
                {1,2,3},
                {1,2,3}
        });
        Matrix3 mR1 = Matrix3.getTransMatrix(m1);
        Matrix3 mR2 = Matrix3.getTransMatrix(m2);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(m1.getMatrix()[row][col], mR2.getMatrix()[row][col]);
                assertEquals(m2.getMatrix()[row][col], mR1.getMatrix()[row][col]);
            }
        }

        m1.transMatrix();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(m2.getMatrix()[row][col], m1.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void zeroMatrixTest() throws Exception {
        Matrix3 m1 = new Matrix3();
        Matrix3 m2 = new Matrix3( new float[][] {
                {0,0,0},
                {0,0,0},
                {0,0,0}
        });
        Matrix3 mR = Matrix3.getZeroMatrix();
        m1.setZero();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(m2.getMatrix()[row][col], mR.getMatrix()[row][col]);
                assertEquals(m2.getMatrix()[row][col], m1.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void identityMatrixTest() throws Exception {
        Matrix3 m1 = new Matrix3();
        Matrix3 m2 = new Matrix3( new float[][] {
                {1,0,0},
                {0,1,0},
                {0,0,1}
        });
        Matrix3 mR = Matrix3.getIdentityMatrix();
        m1.setIdentity();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(m2.getMatrix()[row][col], mR.getMatrix()[row][col]);
                assertEquals(m2.getMatrix()[row][col], m1.getMatrix()[row][col]);
            }
        }
    }

}