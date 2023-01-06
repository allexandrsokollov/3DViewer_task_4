package com.cgvsu.render_engine;



import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3f;

import javax.vecmath.Point2f;

public class GraphicConveyor {

    private static Matrix4 getScaleMatrix(Vector3f scale) {
        Matrix4 matrixScale = new Matrix4();
        matrixScale.setIdentity();
        matrixScale.getMatrix()[0][0] = scale.getX();
        matrixScale.getMatrix()[1][1] = scale.getY();
        matrixScale.getMatrix()[2][2] = scale.getZ();
        return matrixScale;
    }

    public static Matrix4 getRotationMatrix(Vector3f angle) throws Exception {
        if (angle.getX() > 360 || angle.getY() > 360 || angle.getZ() > 360) {
            throw new Exception("The absolute value angle should be less than 360!");
        }
        Matrix4 matrixRotation = new Matrix4();
        matrixRotation.setIdentity();

        Matrix4 matrixRotationX = getRotationMatrixX(angle.getX());
        Matrix4 matrixRotationY = getRotationMatrixY(angle.getY());
        Matrix4 matrixRotationZ = getRotationMatrixZ(angle.getZ());

        matrixRotation.multiply(matrixRotationX);
        matrixRotation.multiply(matrixRotationY);
        matrixRotation.multiply(matrixRotationZ);

        return matrixRotation;
    }

    private static Matrix4 getRotationMatrixX(double xAngle) {
        xAngle = Math.toRadians(xAngle);
        Matrix4 matrixRotationX = new Matrix4();
        matrixRotationX.setIdentity();

        matrixRotationX.getMatrix()[1][1] = (float) Math.cos(xAngle);
        matrixRotationX.getMatrix()[2][2] = (float) Math.cos(xAngle);
        matrixRotationX.getMatrix()[2][1] = (float) Math.sin(xAngle);
        matrixRotationX.getMatrix()[1][2] = (float) (-Math.sin(xAngle));


        return matrixRotationX;
    }

    private static Matrix4 getRotationMatrixY(double yAngle) {
        yAngle = Math.toRadians(yAngle);
        Matrix4 matrixRotationY = new Matrix4();
        matrixRotationY.setIdentity();

        matrixRotationY.getMatrix()[0][0] = (float) Math.cos(yAngle);
        matrixRotationY.getMatrix()[2][2] = (float) Math.cos(yAngle);
        matrixRotationY.getMatrix()[2][0] = (float) (-Math.sin(yAngle));
        matrixRotationY.getMatrix()[0][2] = (float) Math.sin(yAngle);

        return matrixRotationY;
    }

    private static Matrix4 getRotationMatrixZ(double zAngle) {
        zAngle = Math.toRadians(zAngle);
        Matrix4 matrixRotationZ = new Matrix4();
        matrixRotationZ.setIdentity();

        matrixRotationZ.getMatrix()[0][0] = (float) Math.cos(zAngle);
        matrixRotationZ.getMatrix()[1][1] = (float) Math.cos(zAngle);
        matrixRotationZ.getMatrix()[0][1] = (float) (-Math.sin(zAngle));
        matrixRotationZ.getMatrix()[1][0] = (float) Math.sin(zAngle);

        return matrixRotationZ;
    }


    private static Matrix4 getTranslationMatrix(Vector3f translate) {
        Matrix4 matrixTranslation = new Matrix4();
        matrixTranslation.setIdentity();
        matrixTranslation.getMatrix()[3][0] = translate.getX();//Уточнить
        matrixTranslation.getMatrix()[3][1] = translate.getY();
        matrixTranslation.getMatrix()[3][2] = translate.getZ();

        return matrixTranslation;
    }

    public static Matrix4 getModelMatrix(Vector3f translate, Vector3f anglesOfRotate, Vector3f scale) throws Exception {
        Matrix4 modelMatrix = new Matrix4();
        modelMatrix.setIdentity();

        Matrix4 translationMatrix = getTranslationMatrix(translate);
        Matrix4 rotationMatrix = getRotationMatrix(anglesOfRotate);
        Matrix4 scaleMatrix = getScaleMatrix(scale);

        modelMatrix.multiply(translationMatrix);
        modelMatrix.multiply(rotationMatrix);
        modelMatrix.multiply(scaleMatrix);




        return modelMatrix;
    }

    public static Matrix4 lookAt(Vector3f eye, Vector3f target) throws Exception {
        Vector3f up = new Vector3f(0F, 1.0F, 0F);
        /*Vector3f cameraDirection = Vector3f.getSubtracted(eye, target);
        Vector3f cameraRight = Vector3f.getNormalizedVector(Vector3f.getVectorProduct(up, cameraDirection));
        Vector3f cameraUp = Vector3f.getVectorProduct(cameraDirection, cameraRight);*/
        return lookAt(eye, target, up);
    }

    public static Matrix4 lookAt(Vector3f eye, Vector3f target, Vector3f up) throws Exception {
        Vector3f resultZ = Vector3f.getSubtracted(target, eye);
        Vector3f resultX = Vector3f.getVectorProduct(up, resultZ);
        Vector3f resultY = Vector3f.getVectorProduct(resultZ, resultX);



        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();

        float[][] matrix = new float[][]{

                {resultX.getX(), resultY.getX(), resultZ.getX(), 0},
                {resultX.getY(), resultY.getY(), resultZ.getY(), 0},
                {resultX.getZ(), resultY.getZ(), resultZ.getZ(), 0},
                {-resultX.getScalarProduct(eye), -resultY.getScalarProduct(eye), -resultZ.getScalarProduct(eye), 1}};
        return new Matrix4(matrix);
    }

    public static Matrix4 perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4 result = new Matrix4();
        result.setIdentity();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.getMatrix()[0][0] = tangentMinusOnDegree / aspectRatio;
        result.getMatrix()[1][1] = tangentMinusOnDegree;
        result.getMatrix()[2][2] = 2 * (farPlane + nearPlane) / (farPlane - nearPlane);
        result.getMatrix()[2][3] = 1.0F;
        result.getMatrix()[3][2] = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);
        return result;
    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4 matrix, final Vector3f vertex) throws Exception{
        float[][] matrixResult = new float[4][1];
        float[][] vectorMatrix = new float[4][1];
        vectorMatrix[0][0] = vertex.getX();
        vectorMatrix[1][0] = vertex.getY();
        vectorMatrix[2][0] = vertex.getZ();
        vectorMatrix[3][0] = 1;

        for (int row = 0; row < matrix.getMatrix().length; row++) {
            for (int col = 0; col < matrix.getMatrix()[0].length; col++) {
                matrixResult[row][0] += matrix.getMatrix()[row][col] * vectorMatrix[col][0];
            }
        }
        if (matrixResult[3][0] == 0) {
            throw new Exception("Multiplication with this matrix is impossible!");
        }
        return new Vector3f(matrixResult[0][0]/ matrixResult[3][0]
                , matrixResult[1][0]/ matrixResult[3][0], matrixResult[2][0]/ matrixResult[3][0]);
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
