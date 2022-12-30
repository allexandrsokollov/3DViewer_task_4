package com.cgvsu.math;

public class Matrix4 {
    private float[][] matrix = new float[4][4];

    public Matrix4(float[][] matrix) throws Exception {
        if (matrix.length != 4 || matrix[0].length != 4) {
            throw new Exception("Массив не 4 на 4");
        }
        this.matrix = matrix;
    }

    public Matrix4() {
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public void summarize(final Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] += matrix1.getMatrix()[row][col];
            }
        }
    }

    public static Matrix4 getSummarized(final Matrix4 matrix1, final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrixResult.getMatrix()[row][col] = matrix1.getMatrix()[row][col] + matrix2.getMatrix()[row][col];
            }
        }
        return matrixResult;
    }

    public void subtract(final Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] -= matrix1.getMatrix()[row][col];
            }
        }
    }

    public static Matrix4 getSubtracted(final Matrix4 matrix1, final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrixResult.getMatrix()[row][col] = matrix1.getMatrix()[row][col] - matrix2.getMatrix()[row][col];
            }
        }
        return matrixResult;
    }

    public Vector4f getMultiplied(final Vector4f vector) {
        float[][] matrixResult = new float[4][1];
        final float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();
        vectorMatrix[3][0] = vector.getW();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrixResult[row][0] += matrix[row][col] * vectorMatrix[col][0];
            }
        }
        return new Vector4f(matrixResult[0][0], matrixResult[1][0], matrixResult[2][0], matrixResult[3][0]);
    }

    public static Vector4f getMultiplied(final Matrix4 matrix, final Vector4f vector) {
        float[][] matrixResult = new float[4][1];
        float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();
        vectorMatrix[3][0] = vector.getW();

        for (int row = 0; row < matrix.getMatrix().length; row++) {
            for (int col = 0; col < matrix.getMatrix()[0].length; col++) {
                matrixResult[row][0] += matrix.getMatrix()[row][col] * vectorMatrix[col][0];
            }
        }
        return new Vector4f(matrixResult[0][0], matrixResult[1][0], matrixResult[2][0], matrixResult[3][0]);
    }

    public void getMultiplied(final Matrix4 matrix1) {
        float[][] matrixResult = new float[4][4];

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
                for (int i = 0; i < matrix1.getMatrix().length; i++) {
                    matrixResult[row][col] += (matrix[row][i] * matrix1.getMatrix()[i][col]);
                }
            }
        }
        matrix = matrixResult;
    }

    public static Matrix4 getMultiplied(final Matrix4 matrix1, final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int row = 0; row < matrix1.getMatrix().length; row++) {
            for (int col = 0; col < matrix2.getMatrix()[0].length; col++) {
                for (int i = 0; i < matrix2.getMatrix().length; i++) {
                    matrixResult.getMatrix()[row][col] += (matrix1.getMatrix()[row][i] * matrix2.getMatrix()[i][col]);
                }
            }
        }
        return matrixResult;
    }

    public void multiply(final float n) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrix[row][col] *= n;
            }
        }
    }

    public static Matrix4 getMultiplied(final Matrix4 matrix, final float n) {
        Matrix4 matrixResult = new Matrix4();
        for (int row = 0; row < matrix.getMatrix().length; row++) {
            for (int col = 0; col < matrix.getMatrix()[0].length; col++) {
                matrixResult.getMatrix()[row][col] = matrix.getMatrix()[row][col] * n;
            }
        }
        return matrixResult;
    }

    public void transpose() {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix[0].length; col++) {
            for (int row = 0; row < matrix.length; row++) {
                matrixResult.getMatrix()[row][col] = matrix[col][row];
            }
        }
        matrix = matrixResult.getMatrix();
    }

    public static Matrix4 getTransposedMatrix(final Matrix4 matrix1) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrixResult.getMatrix()[col][row] = matrix1.getMatrix()[row][col];
            }
        }
        return matrixResult;
    }

    public void setZero() {
        for (int row = 0; row < matrix[0].length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                matrix[row][col] = 0;
            }
        }
    }

    public static Matrix4 getZeroMatrix() {
        Matrix4 matrix = new Matrix4();
        for (int row = 0; row < matrix.getMatrix()[0].length; row++) {
            for (int col = 0; col < matrix.getMatrix().length; col++) {
                matrix.getMatrix()[row][col] = 0;
            }
        }
        return matrix;
    }

    public void setIdentity() {
        for (int row = 0; row < matrix[0].length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                if(row == col) {
                    matrix[row][col] = 1;
                    continue;
                }
                matrix[row][col] = 0;
            }
        }
    }

    public static Matrix4 getIdentityMatrix() {
        Matrix4 matrix = new Matrix4();
        for (int row = 0; row < matrix.getMatrix()[0].length; row++) {
            for (int col = 0; col < matrix.getMatrix().length; col++) {
                if(row == col) {
                    matrix.getMatrix()[row][col] = 1;
                    continue;
                }
                matrix.getMatrix()[row][col] = 0;
            }
        }
        return matrix;
    }
}
