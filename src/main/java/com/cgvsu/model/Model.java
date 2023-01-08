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
		this.normals = normals;
		recalculateNormals();
	}

	public Model() {
		vertices = new ArrayList<>();
		textureVertices = new ArrayList<>();
		normals = new ArrayList<>();
		polygons = new ArrayList<>();
	}

	public Model(Model model) throws Exception {
		vertices = model.getVertices();
		textureVertices = model.textureVertices;
		normals = model.getNormals();
		polygons = model.getPolygons();
		recalculateNormals();
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

	public void setNormals(List<Vector3f> normals) throws Exception {
		this.normals = normals;
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
		List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
		List<Polygon> triangulatedPolygons = new ArrayList<>();
		if (vertexIndices.size() > 3) {
			for (int i = 2; i < vertexIndices.size(); i++) {
				Polygon triangle = new Polygon();
				triangle.getVertexIndices().add(vertexIndices.get(0));
				triangle.getVertexIndices().add(vertexIndices.get(i - 1));
				triangle.getVertexIndices().add(vertexIndices.get(i));
				triangle.getTextureVertexIndices().add(textureVertexIndices.get(0));
				triangle.getTextureVertexIndices().add(textureVertexIndices.get(i - 1));
				triangle.getTextureVertexIndices().add(textureVertexIndices.get(i));
				triangulatedPolygons.add(triangle);
			}
		} else {
			triangulatedPolygons.add(polygon);
		}
		return triangulatedPolygons;
	}

	protected  void calculateNormalInPolygon(final Polygon polygon, Map<Integer, List<Vector3f>> allNormals){
		List<Integer> vertexIndices = polygon.getVertexIndices();

		Vector3f vector1 = getSubtracted(vertices.get(vertexIndices.get(0)), vertices.get(vertexIndices.get(1)));
		Vector3f vector2 = getSubtracted(vertices.get(vertexIndices.get(0)), vertices.get(vertexIndices.get(2)));

		allNormals.get(vertexIndices.get(0)).add(Vector3f.getVectorProduct(vector1, vector2));
		allNormals.get(vertexIndices.get(1)).add(Vector3f.getVectorProduct(vector1, vector2));
		allNormals.get(vertexIndices.get(2)).add(Vector3f.getVectorProduct(vector1, vector2));
	}

	public void recalculateNormals() throws Exception{
		Map<Integer, List<Vector3f>> allNormals = new HashMap<>(vertices.size());
		for (int i = 0; i < vertices.size(); i++){
			allNormals.put(i, new ArrayList<>());
		}

		for (Polygon polygon : polygons){
			calculateNormalInPolygon(polygon, allNormals);
		}

		Vector3f tmp;

		List<Vector3f> recalculatedNormals = new ArrayList<>();
		for (int i = 0; i < vertices.size(); i++) {
			tmp = new Vector3f(0, 0, 0);
			for (Vector3f normal : allNormals.get(i)) {
				tmp.add(normal);
			}

			tmp.divide(allNormals.get(i).size());
			tmp.normalize();
			recalculatedNormals.add(tmp);
		}
		normals = recalculatedNormals;
	}
}
