package com.cgvsu.render_engine;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cgvsu.math.BarycentricCoordinates;
import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;
import java.awt.Color;

import javax.vecmath.Point2f;

import static com.cgvsu.render_engine.Coloring.convertColorToAWT;
import static com.cgvsu.render_engine.Coloring.getPixelColor;
import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
			final javafx.scene.paint.Color modelColor,
            final BufferedImage texture) throws Exception {
        Matrix4 modelMatrix = getModelMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1,1,1));
        Matrix4 viewMatrix = camera.getViewMatrix();
        Matrix4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4 modelViewProjectionMatrix = new Matrix4(modelMatrix.getMatrix());
        modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix.multiply(projectionMatrix);
        modelViewProjectionMatrix.transpose();

        javafx.scene.paint.Color meshColor = javafx.scene.paint.Color.WHITE;

        float[] zBuffer = new float[width * height];

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

            setRenderingMode(true,
                    true,
                    false,
                    convertColorToAWT(modelColor),
                    meshColor,
                    graphicsContext,
                    texture,
                    width,
                    height,
                    mesh,
                    camera,
                    polygonInd,
                    nVerticesInPolygon,
                    resultPoints,
                    zBuffer);
        }
    }

    private static void setRenderingMode(
            boolean haveSolidColor,
            boolean haveShades,
            boolean haveMesh,
            Color modelColor,
            javafx.scene.paint.Color meshColor,
            GraphicsContext graphicsContext,
            BufferedImage texture,
            int width,
            int height,
            Model mesh,
            Camera camera,
            int polygonInd,
            int nVerticesInPolygon,
            List<Point2f> points,
            float[] zBuffer){

        if (haveSolidColor) {
            rasterizePolygon(
                    true,
                    haveShades,
                    modelColor,
                    graphicsContext,
                    null,
                    width,
                    height,
                    mesh,
                    camera.getTarget(),
                    camera.getPosition(),
                    polygonInd,
                    points,
                    zBuffer);
        } else {
            rasterizePolygon(
                    false,
                    haveShades,
                    modelColor,
                    graphicsContext,
                    texture,
                    width,
                    height,
                    mesh,
                    camera.getTarget(),
                    camera.getPosition(),
                    polygonInd,
                    points,
                    zBuffer);
        }
        if (haveMesh){
            drawMesh(graphicsContext, meshColor, nVerticesInPolygon, points);
        }
    }

    private static void rasterizePolygon(
            boolean haveSolidColor,
            boolean haveShades,
            Color modelColor,
            GraphicsContext graphicsContext,
            BufferedImage texture,
            int width,
            int height,
            Model mesh,
            Vector3f target,
            Vector3f position,
            int polygonInd,
            List<Point2f> points,
            float[] zBuffer){

        Vector3f v1 = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0));
        Vector3f v2 = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1));
        Vector3f v3 = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2));

        float x1 = points.get(0).x;
        float y1 = points.get(0).y;
        float x2 = points.get(1).x;
        float y2 = points.get(1).y;
        float x3 = points.get(2).x;
        float y3 = points.get(2).y;

        int minX = (int) Math.max(0, Math.ceil(Math.min(x1, Math.min(x2, x3))));
        int maxX = (int) Math.min(width - 1, Math.floor(Math.max(x1, Math.max(x2, x3))));
        int minY = (int) Math.max(0, Math.ceil(Math.min(y1, Math.min(y2, y3))));
        int maxY = (int) Math.min(height - 1, Math.floor(Math.max(y1, Math.max(y2, y3))));

        List<Vector3f> polygonNormals = new ArrayList<>();
        polygonNormals.add(mesh.getNormals().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(0)));
        polygonNormals.add(mesh.getNormals().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(1)));
        polygonNormals.add(mesh.getNormals().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(2)));

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                BarycentricCoordinates bCoordinates = new BarycentricCoordinates(x, y, x1, x2, x3, y1, y2, y3);
                if (bCoordinates.getU() >= 0 && bCoordinates.getU() <= 1 && bCoordinates.getV() >= 0 && bCoordinates.getV() <= 1 && bCoordinates.getW() >= 0 && bCoordinates.getW() <= 1) {
                    float depth = bCoordinates.getU() * v1.getZ() + bCoordinates.getV() * v2.getZ() + bCoordinates.getW() * v3.getZ();
                    int zIndex = y * width + x;
                    if (zBuffer[zIndex] < depth) {
                        if (haveSolidColor) {
                            int color = modelColor.getRGB();
                            if (haveShades) {
                                Shadow shadow = new Shadow(polygonNormals, target, position);
                                color = getPixelColor(shadow.calculateShadeCoefficients(bCoordinates), color);
                            }
                            graphicsContext.getPixelWriter().setArgb(x, y, color);
                        }
                        else {
                            Vector2f vt1 = mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(0));
                            Vector2f vt2 = mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(1));
                            Vector2f vt3 = mesh.getTextureVertices().get(mesh.getPolygons().get(polygonInd).getTextureVertexIndices().get(2));

                            float xt = bCoordinates.getU() * vt1.getX() + bCoordinates.getV() * vt2.getX() + bCoordinates.getV() * vt3.getX();
                            float yt = 1 - (bCoordinates.getU() * vt1.getY() + bCoordinates.getV() * vt2.getY() + bCoordinates.getW() * vt3.getY());

                            int color;
                            if (haveShades) {
                                Shadow shadow = new Shadow(polygonNormals, target, position);
                                color = getPixelColor(shadow.calculateShadeCoefficients(bCoordinates), xt, yt, texture);
                            } else {
                                color = getPixelColor(x, y, texture);
                            }
                            graphicsContext.getPixelWriter().setArgb(x, y, color);
                        }
                        zBuffer[zIndex] = depth;
                    }
                }
            }
        }
    }

    private static void drawMesh(
            GraphicsContext graphicsContext,
            javafx.scene.paint.Color meshColor,
            int nVerticesInPolygon,
            List<Point2f> resultPoints){
        graphicsContext.setStroke(meshColor);
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