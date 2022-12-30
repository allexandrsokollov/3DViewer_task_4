package com.cgvsu.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix4Test {
    @Test
    public void sumTest() throws Exception {

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
        Matrix4 mR = Matrix4.sum(m1, m2);
        m1.sum(m2);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m3.getData()[row][col], mR.getData()[row][col]);
                assertEquals(m3.getData()[row][col], m1.getData()[row][col]);
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
        Matrix4 mR = Matrix4.sub(m1, m2);
        m1.sub(m2);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m3.getData()[row][col], mR.getData()[row][col]);
                assertEquals(m3.getData()[row][col], m1.getData()[row][col]);
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
        Vector4f v = Matrix4.multiply(m1, v1);
        assertEquals(vR, v);
        Vector4f v2 = m1.multiply(v1);
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

        Matrix4 m3 = Matrix4.multiply(m1, m2);

        Matrix4 mR = new Matrix4( new float[][] {
                {10, 10, 10, 10},
                {20, 20, 20, 20},
                {30, 30, 30, 30},
                {40, 40, 40, 40}
        });
        m1.multiply(m2);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(mR.getData()[row][col], m1.getData()[row][col]);
                assertEquals(mR.getData()[row][col], m3.getData()[row][col]);
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

        Matrix4 m3 = Matrix4.multiply(m1, n);

        Matrix4 mR = new Matrix4( new float[][] {
                {3,3,3,3},
                {6,6,6,6},
                {9,9,9,9},
                {12,12,12,12}
        });
        m1.multiply(n);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(mR.getData()[row][col], m1.getData()[row][col]);
                assertEquals(mR.getData()[row][col], m3.getData()[row][col]);
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
        Matrix4 mR1 = Matrix4.transpose(m1);
        Matrix4 mR2 = Matrix4.transpose(m2);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m1.getData()[row][col], mR2.getData()[row][col]);
                assertEquals(m2.getData()[row][col], mR1.getData()[row][col]);
            }
        }

        m1.transposeInPlace();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m2.getData()[row][col], m1.getData()[row][col]);
            }
        }
    }

    @Test
    public void zeroTest() throws Exception {
        Matrix4 m1 = new Matrix4();
        Matrix4 m2 = new Matrix4( new float[][] {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        });
        Matrix4 mR = Matrix4.zeroMatrix();
        m1.setZero();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                assertEquals(m2.getData()[row][col], mR.getData()[row][col]);
                assertEquals(m2.getData()[row][col], m1.getData()[row][col]);
            }
        }
    }

    @Test
    public void identityTest() throws Exception {
        Matrix4 m1 = new Matrix4();
        Matrix4 m2 = new Matrix4( new float[][] {
                {1,0,0,0},
                {0,1,0,0},
                {0,0,1,0},
                {0,0,0,1}
        });
        Matrix4 mR = Matrix4.identityMatrix();
        m1.setIdentity();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                assertEquals(m2.getData()[row][col], mR.getData()[row][col]);
                assertEquals(m2.getData()[row][col], m1.getData()[row][col]);
            }
        }
    }
}