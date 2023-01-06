package com.cgvsu.render_engine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector3f;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;
import javafx.scene.paint.Color;

import javax.vecmath.Point2f;

import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
			Color modelColor) throws Exception {
        Matrix4 modelMatrix = getModelMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1));
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4 modelViewProjectionMatrix = new Matrix4(modelMatrix.getMatrix());
        modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix.multiply(projectionMatrix);
        modelViewProjectionMatrix.transpose();//Вопрос

        graphicsContext.setStroke(modelColor);

        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3f vertexVectorMath = new Vector3f(vertex.getX(), vertex.getY(), vertex.getZ());

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVectorMath), width, height);
                resultPoints.add(resultPoint);
            }

            rasterize(graphicsContext, mesh, polygonInd, resultPoints);

            graphicsContext.setStroke(Color.WHITE);
            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).x,
                        resultPoints.get(vertexInPolygonInd - 1).y,
                        resultPoints.get(vertexInPolygonInd).x,
                        resultPoints.get(vertexInPolygonInd).y);
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).x,
                        resultPoints.get(nVerticesInPolygon - 1).y,
                        resultPoints.get(0).x,
                        resultPoints.get(0).y);
        }
    }

    private static void rasterize(GraphicsContext graphicsContext, Model mesh, int polygonInd, List<Point2f> resultPoints){
        BufferedImage img = new BufferedImage((int) graphicsContext.getCanvas().getWidth(), (int) graphicsContext.getCanvas().getHeight(), BufferedImage.TYPE_INT_ARGB);
        float[] zBuffer = new float[img.getWidth() * img.getHeight()];

        Arrays.fill(zBuffer, Float.NEGATIVE_INFINITY);

        rasterizePolygon(graphicsContext,
                img,
                mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)),
                mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)),
                mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)),
                resultPoints,
                zBuffer);
    }

    private static void rasterizePolygon(
            GraphicsContext graphicsContext,
            BufferedImage img,
            Vector3f v1,
            Vector3f v2,
            Vector3f v3,
            List<Point2f> points,
            float[] zBuffer){

        float x1 = points.get(0).x;
        float y1 = points.get(0).y;
        float x2 = points.get(1).x;
        float y2 = points.get(1).y;
        float x3 = points.get(2).x;
        float y3 = points.get(2).y;

        int minX = (int) Math.max(0, Math.ceil(Math.min(x1, Math.min(x2, x3))));
        int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(x1, Math.max(x2, x3))));
        int minY = (int) Math.max(0, Math.ceil(Math.min(y1, Math.min(y2, y3))));
        int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(y1, Math.max(y2, y3))));

        float triangleArea = (y1 - y3) * (x2 - x3) + (y2 - y3) * (x3 - x1);
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                float b1 = ((y - y3) * (x2 - x3) + (y2 - y3) * (x3 - x)) / triangleArea;
                float b2 = ((y - y1) * (x3 - x1) + (y3 - y1) * (x1 - x)) / triangleArea;
                float b3 = ((y - y2) * (x1 - x2) + (y1 - y2) * (x2 - x)) / triangleArea;

                if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                    float depth = b1 * v1.getZ() + b2 * v2.getZ() + b3 * v3.getZ();
                    int zIndex = y * img.getWidth() + x;
                    if (zBuffer[zIndex] < depth) {
                        graphicsContext.setStroke(Color.RED);
                        graphicsContext.strokeLine(x, y, x, y);
                        zBuffer[zIndex] = depth;
                    }
                }
            }
        }
    }
}