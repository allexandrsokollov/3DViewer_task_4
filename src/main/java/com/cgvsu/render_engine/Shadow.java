package com.cgvsu.render_engine;

import com.cgvsu.math.BarycentricCoordinates;
import com.cgvsu.math.Vector3f;

import java.util.List;

public class Shadow {
    float c1;
    float c2;
    float c3;

    public Shadow(List<Vector3f> normals, Vector3f target, Vector3f position){
        this.c1 = calculateShade(normals.get(0), target, position);
        this.c2 = calculateShade(normals.get(1), target, position);
        this.c3 = calculateShade(normals.get(2), target, position);
    }

    private float calculateShade(
            Vector3f normal,
            Vector3f target,
            Vector3f position) {

        Vector3f v = Vector3f.getSubtracted(target, position);
        float cosine = Vector3f.getScalarProduct(Vector3f.getNormalizedVector(normal), Vector3f.getNormalizedVector(v));
        return Float.max(0.8f, Math.abs(cosine));
    }

    public float calculateShadeCoefficients(BarycentricCoordinates bCoordinates){
        return bCoordinates.getU() * c1 + bCoordinates.getV() * c2 + bCoordinates.getW() * c3;
    }
}
