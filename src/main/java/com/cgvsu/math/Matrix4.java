package com.cgvsu.math;

public class Matrix4 {
    private float[][] data = new float[4][4];

    public Matrix4(float[][] matrix) throws Exception {
        if (matrix.length != 4 || matrix[0].length != 4) {
            throw new Exception("Массив не 4 на 4");
        }
        this.data = matrix;
    }

    public Matrix4() {
    }

    public float[][] getData() {
        return data;
    }

    public void sum(final Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                data[row][col] += matrix1.getData()[row][col];
            }
        }
    }

    public static Matrix4 sum(final Matrix4 matrix1, final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                matrixResult.getData()[row][col] = matrix1.getData()[row][col] + matrix2.getData()[row][col];
            }
        }
        return matrixResult;
    }

    public void sub(final Matrix4 matrix1) {
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                data[row][col] -= matrix1.getData()[row][col];
            }
        }
    }

    public static Matrix4 sub(final Matrix4 matrix1, final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                matrixResult.getData()[row][col] = matrix1.getData()[row][col] - matrix2.getData()[row][col];
            }
        }
        return matrixResult;
    }

    public Vector4f multiply(final Vector4f vector) {
        float[][] matrixResult = new float[4][1];
        final float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();
        vectorMatrix[3][0] = vector.getW();

        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[0].length; col++) {
                matrixResult[row][0] += data[row][col] * vectorMatrix[col][0];
            }
        }
        return new Vector4f(matrixResult[0][0], matrixResult[1][0], matrixResult[2][0], matrixResult[3][0]);
    }

    public static Vector4f multiply(final Matrix4 matrix, final Vector4f vector) {
        float[][] matrixResult = new float[4][1];
        float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vector.getX();
        vectorMatrix[1][0] = vector.getY();
        vectorMatrix[2][0] = vector.getZ();
        vectorMatrix[3][0] = vector.getW();

        for (int row = 0; row < matrix.getData().length; row++) {
            for (int col = 0; col < matrix.getData()[0].length; col++) {
                matrixResult[row][0] += matrix.getData()[row][col] * vectorMatrix[col][0];
            }
        }
        return new Vector4f(matrixResult[0][0], matrixResult[1][0], matrixResult[2][0], matrixResult[3][0]);
    }

    public void multiply(final Matrix4 matrix1) {
        float[][] matrixResult = new float[4][4];

        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < matrix1.getData()[0].length; col++) {
                for (int i = 0; i < matrix1.getData().length; i++) {
                    matrixResult[row][col] += (data[row][i] * matrix1.getData()[i][col]);
                }
            }
        }
        data = matrixResult;
    }

    public static Matrix4 multiply(final Matrix4 matrix1,final Matrix4 matrix2) {
        Matrix4 matrixResult = new Matrix4();
        for (int row = 0; row < matrix1.getData().length; row++) {
            for (int col = 0; col < matrix2.getData()[0].length; col++) {
                for (int i = 0; i < matrix2.getData().length; i++) {
                    matrixResult.getData()[row][col] += (matrix1.getData()[row][i] * matrix2.getData()[i][col]);
                }
            }
        }
        return matrixResult;
    }

    public void multiply(final float n) {
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[0].length; col++) {
                data[row][col] *= n;
            }
        }
    }

    public static Matrix4 multiply(final Matrix4 matrix, final float n) {
        Matrix4 matrixResult = new Matrix4();
        for (int row = 0; row < matrix.getData().length; row++) {
            for (int col = 0; col < matrix.getData()[0].length; col++) {
                matrixResult.getData()[row][col] = matrix.getData()[row][col] * n;
            }
        }
        return matrixResult;
    }

    public void transposeInPlace() {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < data[0].length; col++) {
            for (int row = 0; row < data.length; row++) {
                matrixResult.getData()[row][col] = data[col][row];
            }
        }
        data = matrixResult.getData();
    }

    public static Matrix4 transpose(final Matrix4 matrix1) {
        Matrix4 matrixResult = new Matrix4();
        for (int col = 0; col < matrix1.getData()[0].length; col++) {
            for (int row = 0; row < matrix1.getData().length; row++) {
                matrixResult.getData()[col][row] = matrix1.getData()[row][col];
            }
        }
        return matrixResult;
    }

    public void setZero() {
        for (int row = 0; row < data[0].length; row++) {
            for (int col = 0; col < data.length; col++) {
                data[row][col] = 0;
            }
        }
    }

    public static Matrix4 zeroMatrix() {
        Matrix4 matrix = new Matrix4();
        for (int row = 0; row < matrix.getData()[0].length; row++) {
            for (int col = 0; col < matrix.getData().length; col++) {
                matrix.getData()[row][col] = 0;
            }
        }
        return matrix;
    }

    public void setIdentity() {
        for (int row = 0; row < data[0].length; row++) {
            for (int col = 0; col < data.length; col++) {
                if(row == col) {
                    data[row][col] = 1;
                    continue;
                }
                data[row][col] = 0;
            }
        }
    }

    public static Matrix4 identityMatrix() {
        Matrix4 matrix = new Matrix4();
        for (int row = 0; row < matrix.getData()[0].length; row++) {
            for (int col = 0; col < matrix.getData().length; col++) {
                if(row == col) {
                    matrix.getData()[row][col] = 1;
                    continue;
                }
                matrix.getData()[row][col] = 0;
            }
        }
        return matrix;
    }
}
