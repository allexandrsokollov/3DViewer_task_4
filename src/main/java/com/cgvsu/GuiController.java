package com.cgvsu;


import com.cgvsu.model.Model;
import com.cgvsu.objHandlers.ObjReader;
import com.cgvsu.objHandlers.ObjWriter;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.math.Vector3f;

import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.HashMap;
import java.util.Map;

public class GuiController {

    final private float TRANSLATION = 0.5F;
	@FXML
	public Menu modelsMenu;
	@FXML
	public ListView<String> listOfLoadedModelsNames;
	@FXML
	public MenuItem listViewContextDelete;

	private Map<String, Model> editedLoadedModels;
	private Map<String, Model> initialLoadedModels;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Model currentModel = null;

	private String currentModelName = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

	@FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

		System.out.println("initialize called");
		Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
		editedLoadedModels = new HashMap<>();
		initialLoadedModels = new HashMap<>();
		modelsMenu = new Menu();


        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (currentModel != null) {
                try {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, currentModel, (int) width, (int) height);
                } catch (Exception e) {
					Notifications.create()
							.text(e.getMessage())
							.position(Pos.CENTER)
							.showError();
                    throw new RuntimeException(e);
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("objModels"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());
		currentModelName = file.getName();
		System.out.println("file name: " + currentModelName);

		if (! editedLoadedModels.containsKey(currentModelName)) {
			modelsMenu.getItems().add(new MenuItem(currentModelName));
			listOfLoadedModelsNames.getItems().add(currentModelName);
		}

		String fileContent;
		try {
			fileContent = Files.readString(fileName);
		} catch (IOException e) {
			Notifications.create()
					.text(e.getMessage())
					.position(Pos.CENTER)
					.showError();
			throw new RuntimeException(e);
		}
		currentModel = ObjReader.read(fileContent, false);
		editedLoadedModels.put(currentModelName, currentModel);
		initialLoadedModels.put(currentModelName, currentModel.getCopy());
    }

	@FXML
	public void saveModel() {
		saveEditedModel();
	}

	@FXML
	public void selectModel() {
		String modelName = getStringWithoutSurroundingSquareBrackets(
				listOfLoadedModelsNames.getSelectionModel().getSelectedItems().toString());

		Model newModel = editedLoadedModels.get(modelName);

		if (newModel != null && !newModel.equals(currentModel)) {
			currentModel = newModel;
		}
	}

	public void deleteModelFromViewList() {
		String modelName = getStringWithoutSurroundingSquareBrackets(
				listOfLoadedModelsNames.getSelectionModel().getSelectedItems().toString());

		int selectedIndex = listOfLoadedModelsNames.getSelectionModel().getSelectedIndex();
		listOfLoadedModelsNames.getItems().remove(selectedIndex);
		editedLoadedModels.remove(modelName);
		currentModel = null;
	}

	private String getStringWithoutSurroundingSquareBrackets(String initialString) {
		initialString = initialString.substring(1);
		int stringLength = initialString.length();
		return initialString.substring(0, stringLength - 1);
	}

	public void saveInitialModel() {
		saveModel(initialLoadedModels.get(currentModelName));
	}

	public void saveEditedModel() {
		saveModel(editedLoadedModels.get(currentModelName));
	}

	private void saveModel(Model modelToSave) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
		fileChooser.setTitle("Save Model");

		File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
		try {
			ObjWriter.writeToFile(modelToSave, file);
		} catch (IOException e) {
			Notifications.create()
					.text(e.getMessage())
					.position(Pos.CENTER)
					.showError();
			throw new RuntimeException(e);

		}
		Notifications.create()
				.text("File saved at:\n" + file.getAbsolutePath())
				.position(Pos.CENTER)
				.showInformation();
	}

	@FXML
    public void handleCameraForward() {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward() {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft() {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight() {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp() {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown() {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }

}