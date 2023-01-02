package com.cgvsu.objHandlers;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ObjWriter {
	public static void writeToFile(Model model, File file) throws IOException {
		String str = "";

		str += writeVertexes(model.getVertices());
		str += writeTextureVertexes(model.getTextureVertices());
		str += writeNormals(model.getNormals());
		str += writePolygons(model.getPolygons());

		toFile(str, file.getAbsolutePath());
	}

	protected static void toFile(String line, String fileName) throws FileNotFoundException {
		PrintWriter printWriter = new PrintWriter(fileName);
		printWriter.print(line);
		printWriter.close();
	}

	protected static String writeVertexes(List<Vector3f> v){
		StringBuilder str = new StringBuilder();
		for (Vector3f vector3f : v) {
			final String vx = String.format("%.4f", vector3f.x).replace(',', '.');
			final String vy = String.format("%.4f", vector3f.y).replace(',', '.');
			final String vz = String.format("%.4f", vector3f.z).replace(',', '.');
			str.append("v  ").append(vx).append(" ").append(vy).append(" ").append(vz).append("\n");
		}
		str.append("# ").append(v.size()).append(" vertices");
		str.append("\n");
		str.append("\n");
		return str.toString();
	}

	protected static String writeTextureVertexes(List<Vector2f> vt){
		StringBuilder str = new StringBuilder();
		for (Vector2f vector2f : vt) {
			final String vtx = String.format("%.4f", vector2f.getX()).replace(',', '.');
			final String vty = String.format("%.4f", vector2f.getY()).replace(',', '.');
			str.append("vt ").append(vtx).append(" ").append(vty).append(" ").append("0.0000").append("\n");
		}
		str.append("# ").append(vt.size()).append(" texture coords");
		str.append("\n");
		str.append("\n");
		return str.toString();
	}

	protected static String writeNormals(List<Vector3f> vn){
		StringBuilder str = new StringBuilder();
		for (Vector3f vector3f : vn) {
			final String vx = String.format("%.4f", vector3f.x).replace(',', '.');
			final String vy = String.format("%.4f", vector3f.y).replace(',', '.');
			final String vz = String.format("%.4f", vector3f.z).replace(',', '.');
			str.append("vn  ").append(vx).append(" ").append(vy).append(" ").append(vz).append("\n");
		}
		str.append("# ").append(vn.size()).append(" normals");
		str.append("\n");
		str.append("\n");
		return str.toString();
	}

	protected static String writePolygons(List<Polygon> p){
		StringBuilder str = new StringBuilder();
		for (Polygon polygon : p) {
			str.append("f ");
			for (int j = 0; j < polygon.getVertexIndices().size(); j++) {
				if (! polygon.getTextureVertexIndices().isEmpty() && polygon.getNormalIndices().isEmpty()) {
					str.append(polygon.getVertexIndices().get(j) + 1).append("/").append(polygon.getTextureVertexIndices().get(j) + 1).append(" ");
				}
				if (polygon.getTextureVertexIndices().isEmpty() && polygon.getNormalIndices().isEmpty()) {
					str.append(polygon.getVertexIndices().get(j) + 1).append(" ");
				}
				if (! polygon.getTextureVertexIndices().isEmpty() && ! polygon.getNormalIndices().isEmpty()) {
					str.append(polygon.getVertexIndices().get(j) + 1).append("/").append(polygon.getTextureVertexIndices().get(j) + 1).append("/").append(polygon.getNormalIndices().get(j) + 1).append(" ");
				}
				if (polygon.getTextureVertexIndices().isEmpty() && ! polygon.getNormalIndices().isEmpty()) {
					str.append(polygon.getVertexIndices().get(j) + 1).append("//").append(polygon.getNormalIndices().get(j) + 1).append(" ");
				}
			}
			str.append("\n");
		}
		str.append("# ").append(p.size()).append(" polygons");
		str.append("\n");
		str.append("\n");
		return str.toString();
	}
}
