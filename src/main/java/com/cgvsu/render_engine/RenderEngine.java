package com.cgvsu.render_engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;
import java.awt.Color;

import javax.vecmath.Point2f;

import static com.cgvsu.render_engine.GraphicConveyor.*;
import static javax.imageio.ImageIO.read;

public class RenderEngine {

    static BufferedImage img;

    static {
        try {
            img = read(new File("I:\\Programming\\Repository\\3DViewer_task_4\\objModels\\AlexWithTexture\\NeutralWrapped.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static float shade(
            Vector3f normal,
            Camera camera) {

        Vector3f v = Vector3f.getSubtracted(camera.getTarget(), camera.getPosition());
        float cosine = Vector3f.getScalarProduct(Vector3f.getNormalizedVector(normal), Vector3f.getNormalizedVector(v));
        return Float.max(0.8f, Math.abs(cosine));
    }

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
			javafx.scene.paint.Color modelColor) throws Exception {
        Matrix4 modelMatrix = getModelMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1));
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4 modelViewProjectionMatrix = new Matrix4(modelMatrix.getMatrix());
        modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix.multiply(projectionMatrix);
        modelViewProjectionMatrix.transpose();

        graphicsContext.setStroke(modelColor);

        BufferedImage image = new BufferedImage((int) graphicsContext.getCanvas().getWidth(), (int) graphicsContext.getCanvas().getHeight(), BufferedImage.TYPE_INT_ARGB);
        float[] zBuffer = new float[image.getWidth() * image.getHeight()];

        Arrays.fill(zBuffer, Float.NEGATIVE_INFINITY);

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

            float k1 = shade(mesh.getNormals().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)), camera);
            float k2 = shade(mesh.getNormals().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)), camera);
            float k3 = shade(mesh.getNormals().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)), camera);

            applyTextureToPolygon(graphicsContext,
                    image,
                    img,
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)),
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)),
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)),
                    mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(0)),
                    mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(1)),
                    mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(2)),
                    k1,
                    k2,
                    k3,
                    resultPoints,
                    zBuffer);

            /*applyTextureToPolygon(graphicsContext,
                    image,
                    img,
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)),
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)),
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)),
                    mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(0)),
                    mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(1)),
                    mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(2)),
                    resultPoints,
                    zBuffer);*/

            /*rasterizePolygon(graphicsContext,
                    img,
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)),
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)),
                    mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)),
                    resultPoints,
                    zBuffer);

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
                        resultPoints.get(0).y);*/
        }
    }

    private static void applyTextureToPolygon(
            GraphicsContext graphicsContext,
            BufferedImage img,
            BufferedImage texture,
            Vector3f v1,
            Vector3f v2,
            Vector3f v3,
            Vector2f vt1,
            Vector2f vt2,
            Vector2f vt3,
            float k1,
            float k2,
            float k3,
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

                float xt = b1 * vt1.getX() + b2 * vt2.getX() + b3 * vt3.getX();
                float yt = 1 - (b1 * vt1.getY() + b2 * vt2.getY() + b3 * vt3.getY());

                float shadeCoef = b1 * k1 + b2 * k2 + b3 * k3;

                if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                    float depth = b1 * v1.getZ() + b2 * v2.getZ() + b3 * v3.getZ();
                    int zIndex = y * img.getWidth() + x;
                    if (zBuffer[zIndex] < depth) {
                        //int color = texture.getRGB((int) ((xt * ((float) texture.getWidth()))), (int) (yt * (float) (texture.getHeight())));
                        int color = getPixelColor(shadeCoef, xt, yt, texture);
                        graphicsContext.getPixelWriter().setArgb(x, y, color);
                        zBuffer[zIndex] = depth;
                    }
                }
            }
        }
    }

    private static int getPixelColor(
            final float shade,
            final float x,
            final float y,
            BufferedImage img) {
        int color = img.getRGB((int) ((x * ((float) img.getWidth()))), (int) (y * (float) (img.getHeight())));
        int r = (int) (((color >> 16) & 0xff) * shade);
        int g = (int) (((color >> 8) & 0xff) * shade);
        int b = (int) (((color) & 0xff) * shade);
        return new Color(r, g, b).getRGB();
    }

    private static void rasterizePolygon(
            GraphicsContext graphicsContext,
            BufferedImage img, //ширина и высота Canvas
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
                        graphicsContext.getPixelWriter().setArgb(x, y, java.awt.Color.RED.getRGB());
                        zBuffer[zIndex] = depth;
                    }
                }
            }
        }
    }
}