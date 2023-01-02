package com.cgvsu.model;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ModelTest {

	@Test
	void getCopyTest() throws Exception {
		Model initial = new Model(List.of(
				new Vector3f(1, 1, 1)),
				List.of(new Vector2f(1 ,1)),
				List.of(new Vector3f(1,1,1)),
				List.of(new Polygon(
						List.of(1, 1, 1), List.of(1, 1, 1), List.of(1, 1, 1))));
		Model expected = new Model(List.of(
				new Vector3f(1, 1, 1)),
				List.of(new Vector2f(1 ,1)),
				List.of(new Vector3f(1,1,1)),
				List.of(new Polygon(
						List.of(1, 1, 1), List.of(1, 1, 1), List.of(1, 1, 1))));
		Model copied = initial.getCopy();

		initial.setNormals();

		Assertions.assertEquals(expected.getNormals(), copied.getNormals());
	}
}