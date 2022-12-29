package com.cgvsu.math;

public class Matrix3 {
    private float[][] matrix;

    public Matrix3() {
        this.matrix = new float[3][3];
    }

    public Matrix3(float[][] matrix) {
        this.matrix = matrix;
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public void sum(final Matrix3 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] += matrix1.getMatrix()[row][col];
            }
        }
    }

    public static Matrix3 sum(final Matrix3 matrix1, final Matrix3 matrix2) {
        Matrix3 matrixResult = new Matrix3();
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrixResult.getMatrix()[row][col] = matrix1.getMatrix()[row][col] + matrix2.getMatrix()[row][col];
            }
        }
        return matrixResult;
    }

    public void sub(final Matrix3 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] -= matrix1.getMatrix()[row][col];
            }
        }
    }

    public static Matrix3 sub(final Matrix3 matrix1, final Matrix3 matrix2) {
        Matrix3 matrixResult = new Matrix3();
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrixResult.getMatrix()[row][col] = matrix1.getMatrix()[row][col] - matrix2.getMatrix()[row][col];
            }
        }
        return matrixResult;
    }

    public float[][] multiply(final Vector3f vector) {
        float[][] matrixResult = new float[3][1];
        float[][] vectorMatrix = new float[3][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrixResult[row][0] += matrix[row][col] * vectorMatrix[col][0];
            }
        }
        return matrixResult;
    }

    public static float[][] multiply(final Matrix3 matrix, final Vector3f vector) {
        float[][] matrixResult = new float[3][1];
        float[][] vectorMatrix = new float[3][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();

        for (int row = 0; row < matrix.getMatrix().length; row++) {
            for (int col = 0; col < matrix.getMatrix()[0].length; col++) {
                matrixResult[row][0] += matrix.getMatrix()[row][col] * vectorMatrix[col][0];
            }
        }
        return matrixResult;
    }

    public void multiply(final Matrix3 matrix1) {
        float[][] matrixResult = new float[3][3];
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
                for (int i = 0; i < matrix1.getMatrix().length; i++) {
                    matrixResult[row][col] += (matrix[row][i] * matrix1.getMatrix()[i][col]);
                }
            }
        }
        matrix = matrixResult;
    }

    public static Matrix3 multiply(final Matrix3 matrix1, final Matrix3 matrix2) {
        Matrix3 matrixResult = new Matrix3();
        for (int row = 0; row < matrix1.getMatrix().length; row++) {
            for (int col = 0; col < matrix2.getMatrix()[0].length; col++) {
                for (int i = 0; i < matrix2.getMatrix().length; i++) {
                    matrixResult.getMatrix()[row][col] += (matrix1.getMatrix()[row][i] * matrix2.getMatrix()[i][col]);
                }
            }
        }
        return matrixResult;
    }

    public void transMatrix() {
        float[][] matrixResult = new float[3][3];
        for (int col = 0; col < matrix[0].length; col++) {
            for (int row = 0; row < matrix.length; row++) {
                matrixResult[col][row] = matrix[row][col];
            }
        }
        matrix = matrixResult;
    }

    public static Matrix3 transMatrix(final Matrix3 matrix1) {
        Matrix3 matrixResult = new Matrix3();
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

    public static Matrix3 zeroMatrix() {
        Matrix3 matrixResult = new Matrix3();
        for (int row = 0; row < matrixResult.getMatrix()[0].length; row++) {
            for (int col = 0; col < matrixResult.getMatrix().length; col++) {
                matrixResult.getMatrix()[row][col] = 0;
            }
        }
        return matrixResult;
    }

    public void setIdentity() {
        for (int row = 0; row < matrix[0].length; row++) {
            for (int col = 0; col < matrix.length; col++) {
                if(row == col){
                    matrix[row][col] = 1;
                    continue;
                }
                matrix[row][col] = 0;
            }
        }
    }

    public static Matrix3 identityMatrix() {
        Matrix3 matrixResult = new Matrix3();
        for (int row = 0; row < matrixResult.getMatrix()[0].length; row++) {
            for (int col = 0; col < matrixResult.getMatrix().length; col++) {
                if(row == col){
                    matrixResult.getMatrix()[row][col] = 1;
                    continue;
                }
                matrixResult.getMatrix()[row][col] = 0;
            }
        }
        return matrixResult;
    }
}
