package com.cgvsu.render_engine;

import com.cgvsu.model.Model;

import java.util.LinkedList;
import java.util.List;


public class Scene {

	private List<Model> editedLoadedModels;
	private List<Model> initialLoadedModels;
	private List<Model> activeModels;

	private List<String> modelNames = new LinkedList<>();

	public List<Model> getEditedLoadedModels() {
		return editedLoadedModels;
	}

	public void setEditedLoadedModels(List<Model> editedLoadedModels) {
		this.editedLoadedModels = editedLoadedModels;
	}

	public List<Model> getInitialLoadedModels() {
		return initialLoadedModels;
	}

	public void setInitialLoadedModels(List<Model> initialLoadedModels) {
		this.initialLoadedModels = initialLoadedModels;
	}

	public List<Model> getActiveModels() {
		return activeModels;
	}

	public void setActiveModels(List<Model> activeModels) {
		this.activeModels = activeModels;
	}

	public List<String> getModelNames() {
		return modelNames;
	}
}
