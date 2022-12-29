package com.cgvsu.objHandlers;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Polygon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ObjWriterTest {
	@Test
	void getVertexes() {
		ArrayList<Vector3f> vs = new ArrayList<>();
		Vector3f v1 = new Vector3f(1.0F, 2.2F, 4.3F);
		vs.add(v1);
		String res = ObjWriter.writeVertexes(vs);
		String expected = """
				v  1.0000 2.2000 4.3000
				# 1 vertices

				""";
		Assertions.assertEquals(expected, res);
	}

	@Test
	void getTextureVertexes() {
		ArrayList<Vector2f> vt = new ArrayList<>();
		Vector2f v1 = new Vector2f(1.0F, 2.2F);
		vt.add(v1);
		String res = ObjWriter.writeTextureVertexes(vt);
		String expected = """
				vt 1.0000 2.2000 0.0000
				# 1 texture coords

				""";
		Assertions.assertEquals(expected, res);
	}

	@Test
	void getNormals() {
		ArrayList<Vector3f> vn = new ArrayList<>();
		Vector3f v1 = new Vector3f(1.0F, 2.2F, 5.23F);
		vn.add(v1);
		String res = ObjWriter.writeNormals(vn);
		String expected = """
				vn  1.0000 2.2000 5.2300
				# 1 normals

				""";
		Assertions.assertEquals(expected, res);
	}

	@Test
	void getPolygons1() {

		ArrayList<Polygon> pols = new ArrayList<>();
		Polygon p1 = new Polygon();
		p1.addVertexIndex(0);
		p1.addVertexIndex(1);
		p1.addVertexIndex(2);
		pols.add(p1);

		String res = ObjWriter.writePolygons(pols);
		String expected = """
				f 1 2 3\s
				# 1 polygons

				""";
		Assertions.assertEquals(expected, res);
	}

	@Test
	void getPolygons2() {
		ArrayList<Polygon> pols = new ArrayList<>();
		Polygon p1 = new Polygon();
		p1.addVertexIndex(0);
		p1.addVertexIndex(1);
		p1.addVertexIndex(2);
		p1.addNormalIndex(0);
		p1.addNormalIndex(1);
		p1.addNormalIndex(2);

		pols.add(p1);

		String res = ObjWriter.writePolygons(pols);
		String expected = """
				f 1//1 2//2 3//3\s
				# 1 polygons

				""";
		Assertions.assertEquals(expected, res);
	}

	@Test
	void getPolygons3() {
		ArrayList<Polygon> pols = new ArrayList<>();
		Polygon p1 = new Polygon();
		p1.addVertexIndex(0);
		p1.addVertexIndex(1);
		p1.addVertexIndex(2);
		p1.addNormalIndex(0);
		p1.addNormalIndex(1);
		p1.addNormalIndex(2);
		p1.addTextureVertexIndex(0);
		p1.addTextureVertexIndex(1);
		p1.addTextureVertexIndex(2);

		pols.add(p1);

		String res = ObjWriter.writePolygons(pols);
		String expected = """
				f 1/1/1 2/2/2 3/3/3\s
				# 1 polygons

				""";
		Assertions.assertEquals(expected, res);
	}

	@Test
	void getPolygons4() {
		ArrayList<Polygon> pols = new ArrayList<>();
		Polygon p1 = new Polygon();
		p1.addVertexIndex(0);
		p1.addVertexIndex(1);
		p1.addVertexIndex(2);
		p1.addTextureVertexIndex(0);
		p1.addTextureVertexIndex(1);
		p1.addTextureVertexIndex(2);

		pols.add(p1);

		String res = ObjWriter.writePolygons(pols);
		String expected = """
				f 1/1 2/2 3/3\s
				# 1 polygons

				""";
		Assertions.assertEquals(expected, res);
	}

}