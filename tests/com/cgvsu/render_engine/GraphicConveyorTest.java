package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GraphicConveyorTest {

    @Test
    void getModelMatrix() throws Exception {
        Vector3f vector = new Vector3f(1,1,1);

        Matrix4 matrix = GraphicConveyor.getModelMatrix(new Vector3f(1,1,1), new Vector3f(0,0,0),new Vector3f(1,1,1));
        matrix.transpose();
        Vector3f vectorT = GraphicConveyor.multiplyMatrix4ByVector3(matrix, vector);
        vector.add(new Vector3f(1,1,1) );
        assertEquals(vector, vectorT);

        matrix = GraphicConveyor.getModelMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0),new Vector3f(5,5,5));
        matrix.transpose();

        vectorT = GraphicConveyor.multiplyMatrix4ByVector3(matrix, vector);
        assertEquals(Vector3f.getMultipliedVector(vector, 5), vectorT);

        matrix = GraphicConveyor.getModelMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0),new Vector3f(0,0,0));
        matrix.transpose();

        vectorT = GraphicConveyor.multiplyMatrix4ByVector3(matrix, vector);
        assertEquals(Vector3f.getMultipliedVector(vector, 0), vectorT);

        matrix = GraphicConveyor.getModelMatrix(new Vector3f(0,0,0), new Vector3f(90,90,90),new Vector3f(1,1,1));
        //todo сделать тест на матрицу вращения если получится


    }

    @Test
    void multiplyMatrix4ByVector3() throws Exception {
        Vector3f vector = new Vector3f(1,1,1);

        Matrix4 matrix = new Matrix4(new float[][] {
                {1,0,0,0},
                {0,1,0,0},
                {0,0,1,0},
                {0,0,0,1}
        });
        Vector3f vector1 = GraphicConveyor.multiplyMatrix4ByVector3(matrix, vector);
        assertEquals(vector, vector1);

        Matrix4 matrix1 = new Matrix4(new float[][] {
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}
        });
        Exception exception = assertThrows(Exception.class, () -> {
            GraphicConveyor.multiplyMatrix4ByVector3(matrix1, vector);
        });

        String eS = "Multiplication with this matrix is impossible!";
        String act = exception.getMessage();
        assertTrue(act.contains(eS));

        Matrix4 matrix2 = new Matrix4(new float[][] {
                {1,0,0,1},
                {0,1,0,1},
                {0,0,1,1},
                {0,0,0,1}
        });
        Vector3f vector2 = GraphicConveyor.multiplyMatrix4ByVector3(matrix2, vector);
        Vector3f vector3 = new Vector3f(2, 2, 2);
        assertEquals(vector3, vector2);
    }
}