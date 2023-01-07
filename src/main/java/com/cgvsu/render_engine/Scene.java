package com.cgvsu.render_engine;

import com.cgvsu.model.ModifiedModel;

import java.util.LinkedList;
import java.util.List;


public class Scene {

	private List<ModifiedModel> models;
	private List<ModifiedModel> activeModels;
	private final List<String> modelNames;

	public Scene() {
		models = new LinkedList<>();
		activeModels = new LinkedList<>();
		modelNames = new LinkedList<>();
	}

	public void deleteSelectedModels(List<Integer> indexesToDelete, List<String> namesToDelete) {
		int decrement = 0;
		for (int index : indexesToDelete) {
			getModels().remove(index - decrement++);
		}

		getActiveModels().removeIf(model -> ! getModels().contains(model));
		getModelNames().removeIf(namesToDelete::contains);
	}

	public List<ModifiedModel> getModels() {
		return models;
	}

	public void setModels(List<ModifiedModel> models) {
		this.models = models;
	}

	public List<ModifiedModel> getActiveModels() {
		return activeModels;
	}

	public void setActiveModels(List<ModifiedModel> activeModels) {
		this.activeModels = activeModels;
	}

	public List<String> getModelNames() {
		return modelNames;
	}
}
