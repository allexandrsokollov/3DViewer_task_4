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
            final BufferedImage texture,
            final boolean[] renderingStatements) throws Exception {
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

            startRender(renderingStatements[0],
                    renderingStatements[1],
                    renderingStatements[2],
                    renderingStatements[3],
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

    private static void startRender(
            boolean haveSolidColor,
            boolean haveTexture,
            boolean haveShade,
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

        if (haveTexture) {
            rasterizePolygon(
                    false,
                    true,
                    haveShade,
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
        if (haveSolidColor){
            rasterizePolygon(
                    true,
                    false,
                    haveShade,
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
        }
        if (haveMesh){
            drawMesh(graphicsContext, meshColor, nVerticesInPolygon, points);
        }
    }

    private static void rasterizePolygon(
            boolean haveSolidColor,
            boolean haveTexture,
            boolean haveShade,
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

        List<Integer> vertexIndices = mesh.getPolygons().get(polygonInd).getVertexIndices();
        Vector3f v[] = new Vector3f[] {mesh.getVertices().get(vertexIndices.get(0)), mesh.getVertices().get(vertexIndices.get(1)), mesh.getVertices().get(vertexIndices.get(2))};

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

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                BarycentricCoordinates bCoordinates = new BarycentricCoordinates(x, y, x1, x2, x3, y1, y2, y3);
                if (bCoordinates.getU() >= 0 && bCoordinates.getU() <= 1 && bCoordinates.getV() >= 0 && bCoordinates.getV() <= 1 && bCoordinates.getW() >= 0 && bCoordinates.getW() <= 1) {
                    float depth = bCoordinates.getU() * v[0].getZ() + bCoordinates.getV() * v[1].getZ() + bCoordinates.getW() * v[2].getZ();
                    int zIndex = y * width + x;
                    if (zBuffer[zIndex] < depth) {
                        if (haveTexture) {
                            if (haveShade){
                                Vector3f vn[] = new Vector3f[] {mesh.getNormals().get(vertexIndices.get(0)), mesh.getNormals().get(vertexIndices.get(1)), mesh.getNormals().get(vertexIndices.get(2))};
                                List<Integer> textureVertexIndices = mesh.getPolygons().get(polygonInd).getTextureVertexIndices();
                                Vector2f[] vt = new Vector2f[] {mesh.getTextureVertices().get(textureVertexIndices.get(0)), mesh.getTextureVertices().get(textureVertexIndices.get(1)), mesh.getTextureVertices().get(textureVertexIndices.get(2))};

                                float xt = bCoordinates.getU() * vt[0].getX() + bCoordinates.getV() * vt[1].getX() + bCoordinates.getV() * vt[2].getX();
                                float yt = 1 - (bCoordinates.getU() * vt[0].getY() + bCoordinates.getV() * vt[1].getY() + bCoordinates.getW() * vt[2].getY());

                                drawPixel(graphicsContext, x, y, xt, yt, bCoordinates, vn, target, position, texture);
                            }
                            else {
                                drawPixel(graphicsContext, x, y, texture);
                            }
                        } else {
                            if (haveSolidColor) {
                                int color = modelColor.getRGB();
                                if (haveShade) {
                                    Vector3f vn[] = new Vector3f[]{mesh.getNormals().get(vertexIndices.get(0)), mesh.getNormals().get(vertexIndices.get(1)), mesh.getNormals().get(vertexIndices.get(2))};
                                    drawPixel(graphicsContext, x, y, bCoordinates, vn, target, position, color);
                                } else {
                                    drawPixel(graphicsContext, x, y, color);
                                }
                            }
                        }
                        zBuffer[zIndex] = depth;
                    }
                }
            }
        }
    }

    private static void drawPixel(GraphicsContext graphicsContext, int x, int y, int color){
        graphicsContext.getPixelWriter().setArgb(x, y, color);
    }
    private static void drawPixel(GraphicsContext graphicsContext, int x, int y, BarycentricCoordinates bCoordinates, Vector3f[] polygonNormals, Vector3f target, Vector3f position, int color){
        Shadow shadow = new Shadow(polygonNormals, target, position);
        color = getPixelColor(shadow.calculateShadeCoefficients(bCoordinates), color);
        graphicsContext.getPixelWriter().setArgb(x, y, color);
    }

    private static void drawPixel(GraphicsContext graphicsContext, int x, int y, BufferedImage texture){
        int color = getPixelColor(x, y, texture);
        graphicsContext.getPixelWriter().setArgb(x, y, color);
    }
    private static void drawPixel(GraphicsContext graphicsContext, int x, int y, float xt, float yt, BarycentricCoordinates bCoordinates, Vector3f[] polygonNormals, Vector3f target, Vector3f position, BufferedImage texture) {
        Shadow shadow = new Shadow(polygonNormals, target, position);
        int color = getPixelColor(shadow.calculateShadeCoefficients(bCoordinates), xt, yt, texture);
        graphicsContext.getPixelWriter().setArgb(x, y, color);
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