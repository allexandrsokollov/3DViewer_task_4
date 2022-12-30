package com.cgvsu.math;

public class Matrix3 {
    private float[][] matrix = new float[3][3];

    public Matrix3() {
    }

    public Matrix3(float[][] matrix) throws Exception {
        if (matrix.length != 3 || matrix[0].length != 3) {
            throw new Exception("Массив не 3 на 3");
        }
        this.matrix = matrix;
    }

    public float[][] getMatrix() {
        return matrix;
    }

    public void add(final Matrix3 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] += matrix1.getMatrix()[row][col];
            }
        }
    }

    public static Matrix3 getAdded(final Matrix3 matrix1, final Matrix3 matrix2) {
        Matrix3 matrixResult = new Matrix3();
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrixResult.getMatrix()[row][col] = matrix1.getMatrix()[row][col] + matrix2.getMatrix()[row][col];
            }
        }
        return matrixResult;
    }

    public void subtract(final Matrix3 matrix1) {
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrix[row][col] -= matrix1.getMatrix()[row][col];
            }
        }
    }

    public static Matrix3 getSubtracted(final Matrix3 matrix1, final Matrix3 matrix2) {
        Matrix3 matrixResult = new Matrix3();
        for (int col = 0; col < matrix1.getMatrix()[0].length; col++) {
            for (int row = 0; row < matrix1.getMatrix().length; row++) {
                matrixResult.getMatrix()[row][col] = matrix1.getMatrix()[row][col] - matrix2.getMatrix()[row][col];
            }
        }
        return matrixResult;
    }

    public Vector3f getMultiplied(final Vector3f vector) {
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
        return new Vector3f(matrixResult[0][0], matrixResult[1][0], matrixResult[2][0]);
    }

    public static Vector3f getMultiplied(final Matrix3 matrix, final Vector3f vector) {
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
        return new Vector3f(matrixResult[0][0], matrixResult[1][0], matrixResult[2][0]);
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

    public static Matrix3 getMultiplied(final Matrix3 matrix1, final Matrix3 matrix2) {
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

    public void multiply(final float n) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                matrix[row][col] *= n;
            }
        }
    }

    public static Matrix3 getMultiplied(final Matrix3 matrix, final float n) {
        Matrix3 matrixResult = new Matrix3();
        for (int row = 0; row < matrix.getMatrix().length; row++) {
            for (int col = 0; col < matrix.getMatrix()[0].length; col++) {
                matrixResult.getMatrix()[row][col] = matrix.getMatrix()[row][col] * n;
            }
        }
        return matrixResult;
    }


    public void transpose() {
        float[][] matrixResult = new float[3][3];
        for (int col = 0; col < matrix[0].length; col++) {
            for (int row = 0; row < matrix.length; row++) {
                matrixResult[col][row] = matrix[row][col];
            }
        }
        matrix = matrixResult;
    }

    public static Matrix3 getTransposedMatrix(final Matrix3 matrix1) {
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

    public static Matrix3 getZeroMatrix() {
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

    public static Matrix3 getIdentityMatrix() {
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
