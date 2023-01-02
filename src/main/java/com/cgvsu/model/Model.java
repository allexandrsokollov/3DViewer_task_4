package com.cgvsu.model;
import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.objHandlers.ReaderExceptions;

import java.util.*;

import static com.cgvsu.math.Vector3f.*;

public class Model {
	private List<Vector3f> vertices;
	private List<Vector2f> textureVertices;
	private List<Vector3f> normals;
	private List<Polygon> polygons;

	public Model(final List<Vector3f> vertices, final List<Vector2f> textureVertices, final List<Vector3f> normals, final List<Polygon> polygons) throws Exception {
		this.vertices = vertices;
		this.textureVertices = textureVertices;
		this.polygons = polygons;
		this.normals = new ArrayList<>();
		recalculateNormals();
		triangulate();
	}

	public Model() {
		vertices = new ArrayList<>();
		textureVertices = new ArrayList<>();
		normals = new ArrayList<>();
		polygons = new ArrayList<>();
	}

	public Model getCopy() throws Exception {
		return new Model(
				this.vertices,
				this.textureVertices,
				this.normals,
				this.polygons);
	}

	public List<Vector3f> getVertices() {
		return vertices;
	}

	public List<Vector2f> getTextureVertices() {
		return textureVertices;
	}

	public List<Vector3f> getNormals() {
		return normals;
	}

	public List<Polygon> getPolygons() {
		return polygons;
	}

	public void setVertices(final List<Vector3f> vertices) {
		this.vertices = vertices;
	}

	public void setTextureVertices(final List<Vector2f> vertices) {
		this.textureVertices = vertices;
	}

	public void setNormals() throws Exception {
		recalculateNormals();
	}

	public void setPolygons(final List<Polygon> polygons) throws Exception {
		this.polygons = polygons;
		recalculateNormals();
		triangulate();
	}

	public boolean checkConsistency() {
		for (int i = 0; i < polygons.size(); i++) {
			List<Integer> vertexIndices = polygons.get(i).getVertexIndices();
			List<Integer> textureVertexIndices = polygons.get(i).getTextureVertexIndices();
			List<Integer> normalIndices = polygons.get(i).getNormalIndices();
			if (vertexIndices.size() != textureVertexIndices.size()
					&& vertexIndices.size() != 0 && textureVertexIndices.size() != 0) {
				throw new ReaderExceptions.NotDefinedUniformFormatException(
						"The unified format for specifying polygon descriptions is not defined.");
			}
			if (vertexIndices.size() != normalIndices.size()
					&& vertexIndices.size() != 0 &&  normalIndices.size() != 0) {
				throw new ReaderExceptions.NotDefinedUniformFormatException(
						"The unified format for specifying polygon descriptions is not defined.");
			}
			if (normalIndices.size() != textureVertexIndices.size()
					&& normalIndices.size() != 0 && textureVertexIndices.size() != 0) {
				throw new ReaderExceptions.NotDefinedUniformFormatException(
						"The unified format for specifying polygon descriptions is not defined.");
			}
			for (Integer vertexIndex : vertexIndices) {
				if (vertexIndex >= vertices.size()) {
					throw new ReaderExceptions.FaceException(
							"Polygon description is wrong.", i + 1);
				}
			}
			for (Integer textureVertexIndex : textureVertexIndices) {
				if (textureVertexIndex >= textureVertices.size()) {
					throw new ReaderExceptions.FaceException(
							"Polygon description is wrong.", i + 1);
				}
			}
			for (Integer normalIndex : normalIndices) {
				if (normalIndex >= normals.size()) {
					throw new ReaderExceptions.FaceException(
							"Polygon description is wrong.", i + 1);
				}
			}
		}
		return true;
	}

	public void triangulate() {
		List<Polygon> triangulatedPolygons = new ArrayList<>();
		for (Polygon polygon : polygons) {
			triangulatedPolygons.addAll(triangulatePolygon(polygon));
		}
		polygons = triangulatedPolygons;
	}

	private List<Polygon> triangulatePolygon(Polygon polygon){
		List<Integer> vertexIndices = polygon.getVertexIndices();
		List<Polygon> triangulatedPolygons = new ArrayList<>();
		if (vertexIndices.size() > 3) {
			for (int i = 2; i < vertexIndices.size(); i++) {
				Polygon triangle = new Polygon();
				triangle.getVertexIndices().add(vertexIndices.get(0));
				triangle.getVertexIndices().add(vertexIndices.get(i - 1));
				triangle.getVertexIndices().add(vertexIndices.get(i));
				triangulatedPolygons.add(triangle);
			}
		} else {
			triangulatedPolygons.add(polygon);
		}
		return triangulatedPolygons;
	}

	public void recalculateNormals() throws Exception {
		normals.clear();
		for (int i = 0; i < vertices.size(); i++) {
			normals.add(calculateNormalByVertexInModel(i));
		}
	}

	protected  Vector3f calculateNormalInPolygon(final Polygon polygon){
		List<Integer> vertexIndices = polygon.getVertexIndices();
		int verticesCount = vertexIndices.size();

		Vector3f vector1 = getAdded(vertices.get(vertexIndices.get(0)), vertices.get(vertexIndices.get(1)));
		Vector3f vector2 = getAdded(vertices.get(vertexIndices.get(0)), vertices.get(vertexIndices.get(verticesCount - 1)));

		return getVectorProduct(vector1, vector2);
	}

	protected Vector3f calculateNormalByVertexInModel(final int vertexIndex) throws Exception {
		ArrayList<Vector3f> vector = new ArrayList<>();
		for (Polygon polygon : polygons) {
			if (polygon.getVertexIndices().contains(vertexIndex)) {
				vector.add(calculateNormalInPolygon(polygon));
			}
		}

		float x = vector.get(0).getX();
		float y = vector.get(0).getY();
		float z = vector.get(0).getZ();

		for (int i = 1; i < vector.size(); i++) {
			x += vector.get(i).getX();
			y += vector.get(i).getY();
			z += vector.get(i).getZ();
		}

		Vector3f normal = new Vector3f(x, y, z);
		normal.divide(vector.size());

		return normal;
	}
}
