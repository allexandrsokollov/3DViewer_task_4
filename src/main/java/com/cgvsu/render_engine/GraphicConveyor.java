package com.cgvsu.render_engine;



import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector4f;

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

    private static Matrix4 getRotationMatrix(Vector3f angle) throws Exception {
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

    private static Matrix4 getRotationMatrixX(float xAngle) {
        Matrix4 matrixRotationX = new Matrix4();
        matrixRotationX.setIdentity();

        matrixRotationX.getMatrix()[1][1] = (float) Math.cos(xAngle);
        matrixRotationX.getMatrix()[2][2] = (float) Math.cos(xAngle);
        matrixRotationX.getMatrix()[2][1] = (float) Math.sin(xAngle);
        matrixRotationX.getMatrix()[1][2] = (float) (-Math.sin(xAngle));

        return matrixRotationX;
    }

    private static Matrix4 getRotationMatrixY(float yAngle) {
        Matrix4 matrixRotationY = new Matrix4();
        matrixRotationY.setIdentity();

        matrixRotationY.getMatrix()[0][0] = (float) Math.cos(yAngle);
        matrixRotationY.getMatrix()[2][2] = (float) Math.cos(yAngle);
        matrixRotationY.getMatrix()[2][0] = (float) (-Math.sin(yAngle));
        matrixRotationY.getMatrix()[0][2] = (float) Math.sin(yAngle);

        return matrixRotationY;
    }

    private static Matrix4 getRotationMatrixZ(float zAngle) {
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
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4 lookAt(Vector3f eye, Vector3f target, Vector3f up) throws Exception {
        Vector3f resultZ = Vector3f.getSubtracted(target, eye);
        Vector3f resultX = Vector3f.getVectorProduct(up, resultZ);
        Vector3f resultY = Vector3f.getVectorProduct(resultZ, resultX);;



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
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.getMatrix()[0][0] = tangentMinusOnDegree / aspectRatio;
        result.getMatrix()[1][1] = tangentMinusOnDegree;
        result.getMatrix()[2][2] = (farPlane + nearPlane) / (farPlane - nearPlane);
        result.getMatrix()[2][3] = 1.0F;
        result.getMatrix()[3][2] = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);
        return result;
    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4 matrix, final Vector3f vertex) {
        final float x = (vertex.getX() * matrix.getMatrix()[0][0]) + (vertex.getY() * matrix.getMatrix()[1][0])
                + (vertex.getZ() * matrix.getMatrix()[2][0]) + matrix.getMatrix()[3][0];
        final float y = (vertex.getX() * matrix.getMatrix()[0][1]) + (vertex.getY() * matrix.getMatrix()[1][1])
                + (vertex.getZ() * matrix.getMatrix()[2][1]) + matrix.getMatrix()[3][1];
        final float z = (vertex.getX() * matrix.getMatrix()[0][2]) + (vertex.getY() * matrix.getMatrix()[1][2])
                + (vertex.getZ() * matrix.getMatrix()[2][2]) + matrix.getMatrix()[3][2];
        final float w = (vertex.getX() * matrix.getMatrix()[0][3]) + (vertex.getY() * matrix.getMatrix()[1][3])
                + (vertex.getZ() * matrix.getMatrix()[2][3]) + matrix.getMatrix()[3][3];;
        return new Vector3f(x / w, y / w, z / w);
    }

    public static Vector3f vector4fToVector3f(Vector4f vector4f) {
        final float w = vector4f.getW();
        final float x = vector4f.getX() ;
        final float y = vector4f.getY() ;
        final float z = vector4f.getZ() ;

        return new Vector3f(x, y, z);
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
