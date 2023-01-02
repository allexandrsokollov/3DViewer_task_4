package com.cgvsu.render_engine;



import com.cgvsu.math.Matrix3;
import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3f;

import javax.vecmath.Point2f;

public class GraphicConveyor {

    private static Matrix3 getScaleMatrix(Vector3f scale) {
        Matrix3 matrixScale = new Matrix3();
        matrixScale.setIdentity();
        matrixScale.getMatrix()[0][0] = scale.getX();
        matrixScale.getMatrix()[1][1] = scale.getY();
        matrixScale.getMatrix()[2][2] = scale.getZ();
        return matrixScale;
    }

    private static Matrix3 getRotationMatrix() {
        Matrix3 matrixRotation = new Matrix3();

        return matrixRotation;
    }

    private static Matrix4 getTranslationMatrix() {
        Matrix4 matrixTranslation = new Matrix4();

        return matrixTranslation;
    }

    public static Matrix4 rotateScaleTranslate() throws Exception {
        float[][] matrix = new float[][]{

                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
        return new Matrix4(matrix);
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

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
