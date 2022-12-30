package com.cgvsu.render_engine;



import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3f;

import javax.vecmath.Point2f;

public class GraphicConveyor {

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
        Vector3f resultZ = Vector3f.sub(target, eye);
        Vector3f resultX = Vector3f.vectorProduct(up, resultZ);
        Vector3f resultY = Vector3f.vectorProduct(resultZ, resultX);;



        resultX.normalization();
        resultY.normalization();
        resultZ.normalization();

        float[][] matrix = new float[][]{

                {resultX.x, resultY.x, resultZ.x, 0},
                {resultX.y, resultY.y, resultZ.y, 0},
                {resultX.z, resultY.z, resultZ.z, 0},
                {-resultX.scalarProduct(eye), -resultY.scalarProduct(eye), -resultZ.scalarProduct(eye), 1}};
        return new Matrix4(matrix);
    }

    public static Matrix4 perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4 result = new Matrix4();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.getData()[0][0] = tangentMinusOnDegree / aspectRatio;
        result.getData()[1][1] = tangentMinusOnDegree;
        result.getData()[2][2] = (farPlane + nearPlane) / (farPlane - nearPlane);
        result.getData()[2][3] = 1.0F;
        result.getData()[3][2] = 2 * (nearPlane * farPlane) / (nearPlane - farPlane);
        return result;
    }

    public static Vector3f multiplyMatrix4ByVector3(final Matrix4 matrix, final Vector3f vertex) {
        final float x = (vertex.x * matrix.getData()[0][0]) + (vertex.y * matrix.getData()[1][0])
                + (vertex.z * matrix.getData()[2][0]) + matrix.getData()[3][0];
        final float y = (vertex.x * matrix.getData()[0][1]) + (vertex.y * matrix.getData()[1][1])
                + (vertex.z * matrix.getData()[2][1]) + matrix.getData()[3][1];
        final float z = (vertex.x * matrix.getData()[0][2]) + (vertex.y * matrix.getData()[1][2])
                + (vertex.z * matrix.getData()[2][2]) + matrix.getData()[3][2];
        final float w = (vertex.x * matrix.getData()[0][3]) + (vertex.y * matrix.getData()[1][3])
                + (vertex.z * matrix.getData()[2][3]) + matrix.getData()[3][3];;
        return new Vector3f(x / w, y / w, z / w);
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.x * width + width / 2.0F, -vertex.y * height + height / 2.0F);
    }
}
