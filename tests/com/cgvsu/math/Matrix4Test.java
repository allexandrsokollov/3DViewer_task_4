package com.cgvsu.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix4Test {
    @Test
    public void addTest() throws Exception {

        Matrix4 m1 = new Matrix4( new float[][] {
                {1,1,1,1},
                {2,2,2,2},
                {3,3,3,3},
                {4,4,4,4}
        });
        Matrix4 m2 = new Matrix4( new float[][] {
                {4,4,4,4},
                {3,3,3,3},
                {2,2,2,2},
                {1,1,1,1}
        });
        Matrix4 m3 = new Matrix4( new float[][] {
                {5,5,5,5},
                {5,5,5,5},
                {5,5,5,5},
                {5,5,5,5}
        });
        Matrix4 m0 = new Matrix4( new float[][] {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        });
        Matrix4 mR = Matrix4.getSummarized(m1, m2);

        m1.summarize(m2);
        Matrix4 mR0 = Matrix4.getSummarized(m1, m0);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m3.getMatrix()[row][col], mR.getMatrix()[row][col]);
                assertEquals(m3.getMatrix()[row][col], m1.getMatrix()[row][col]);
                assertEquals(m1.getMatrix()[row][col], mR0.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void subTest() throws Exception {
        Matrix4 m1 = new Matrix4( new float[][] {
                {1,1,1,1},
                {2,2,2,2},
                {3,3,3,3},
                {4,4,4,4}
        });
        Matrix4 m2 = new Matrix4( new float[][] {
                {4,4,4,4},
                {3,3,3,3},
                {2,2,2,2},
                {1,1,1,1}
        });
        Matrix4 m3 = new Matrix4( new float[][] {
                {-3,-3,-3,-3},
                {-1,-1,-1,-1},
                {1,1,1,1},
                {3,3,3,3}
        });
        Matrix4 m0 = new Matrix4( new float[][] {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        });
        Matrix4 mR = Matrix4.getSubtracted(m1, m2);

        m1.subtract(m2);
        Matrix4 mR0 = Matrix4.getSubtracted(m1, m0);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m3.getMatrix()[row][col], mR.getMatrix()[row][col]);
                assertEquals(m3.getMatrix()[row][col], m1.getMatrix()[row][col]);
                assertEquals(m1.getMatrix()[row][col], mR0.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void multiplyVTest() throws Exception {
        Matrix4 m1 = new Matrix4( new float[][] {
                {1,1,1,1},
                {2,2,2,2},
                {3,3,3,3},
                {4,4,4,4}
        });
        Vector4f v1 = new Vector4f(1, 2, 3, 4);
        Vector4f vR = new Vector4f(10, 20, 30, 40);
        Vector4f v = Matrix4.getMultiplied(m1, v1);
        assertEquals(vR, v);
        Vector4f v2 = m1.getMultiplied(v1);
        assertEquals(vR, v2);

    }

    @Test
    public void multiplyMTest() throws Exception {
        Matrix4 m1 = new Matrix4( new float[][] {
                {1,1,1,1},
                {2,2,2,2},
                {3,3,3,3},
                {4,4,4,4}
        });
        Matrix4 m2 = new Matrix4( new float[][] {
                {1,1,1,1},
                {2,2,2,2},
                {3,3,3,3},
                {4,4,4,4}
        });

        Matrix4 m3 = Matrix4.getMultiplied(m1, m2);

        Matrix4 mR = new Matrix4( new float[][] {
                {10, 10, 10, 10},
                {20, 20, 20, 20},
                {30, 30, 30, 30},
                {40, 40, 40, 40}
        });
        m1.getMultiplied(m2);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(mR.getMatrix()[row][col], m1.getMatrix()[row][col]);
                assertEquals(mR.getMatrix()[row][col], m3.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void multiplyNTest() throws Exception {
        Matrix4 m1 = new Matrix4( new float[][] {
                {1,1,1,1},
                {2,2,2,2},
                {3,3,3,3},
                {4,4,4,4}
        });
        float n = 3;

        Matrix4 m3 = Matrix4.getMultiplied(m1, n);

        Matrix4 mR = new Matrix4( new float[][] {
                {3,3,3,3},
                {6,6,6,6},
                {9,9,9,9},
                {12,12,12,12}
        });
        m1.multiply(n);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(mR.getMatrix()[row][col], m1.getMatrix()[row][col]);
                assertEquals(mR.getMatrix()[row][col], m3.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void transMatrixTest() throws Exception {
        Matrix4 m1 = new Matrix4( new float[][] {
                {1,1,1,1},
                {2,2,2,2},
                {3,3,3,3},
                {4,4,4,4}
        });
        Matrix4 m2 = new Matrix4( new float[][] {
                {1,2,3,4},
                {1,2,3,4},
                {1,2,3,4},
                {1,2,3,4}
        });
        Matrix4 mR1 = Matrix4.getTransposedMatrix(m1);
        Matrix4 mR2 = Matrix4.getTransposedMatrix(m2);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m1.getMatrix()[row][col], mR2.getMatrix()[row][col]);
                assertEquals(m2.getMatrix()[row][col], mR1.getMatrix()[row][col]);
            }
        }

        m1.transpose();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m2.getMatrix()[row][col], m1.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void zeroMatrixTest() throws Exception {
        Matrix4 m1 = new Matrix4();
        Matrix4 m2 = new Matrix4( new float[][] {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        });
        Matrix4 mR = Matrix4.getZeroMatrix();
        m1.setZero();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m2.getMatrix()[row][col], mR.getMatrix()[row][col]);
                assertEquals(m2.getMatrix()[row][col], m1.getMatrix()[row][col]);
            }
        }
    }

    @Test
    public void identityMatrixTest() throws Exception {
        Matrix4 m1 = new Matrix4();
        Matrix4 m2 = new Matrix4( new float[][] {
                {1,0,0,0},
                {0,1,0,0},
                {0,0,1,0},
                {0,0,0,1}
        });
        Matrix4 mR = Matrix4.getIdentityMatrix();
        m1.setIdentity();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(m2.getMatrix()[row][col], mR.getMatrix()[row][col]);
                assertEquals(m2.getMatrix()[row][col], m1.getMatrix()[row][col]);
            }
        }
    }
}