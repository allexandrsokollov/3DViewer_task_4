package com.cgvsu;

import com.cgvsu.math.Matrix4;
import com.cgvsu.math.Vector2f;
import com.cgvsu.model.Model;
import com.cgvsu.model.ModifiedModel;
import com.cgvsu.objHandlers.ObjReader;
import com.cgvsu.objHandlers.ObjWriter;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.math.Vector3f;

import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.Scene;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.LinkedList;
import java.util.List;

import static com.cgvsu.render_engine.GraphicConveyor.getModelMatrix;

public class GuiController {

    final private float TRANSLATION = 0.8F;

	@FXML
	public Menu modelsMenu;
	@FXML
	public ListView<String> listOfLoadedModelsNames;
	@FXML
	public MenuItem listViewContextDelete;
	@FXML
	public Spinner<Double> spinnerMoveX;
	@FXML
	public Spinner<Double> spinnerMoveY;
	@FXML
	public Spinner<Double> spinnerMoveZ;
	@FXML
	public Spinner<Double> spinnerScaleX;
	@FXML
	public Spinner<Double> spinnerScaleY;
	@FXML
	public Spinner<Double> spinnerScaleZ;
	@FXML
	public Spinner<Double> spinnerRotateX;
	@FXML
	public Spinner<Double> spinnerRotateY;
	@FXML
	public Spinner<Double> spinnerRotateZ;
	@FXML
	public Button buttonApplyTransformation;
	@FXML
	public Pane controlPane;
	@FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
	private Scene scene;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

	@FXML
    private void initialize() {
		initFXMLComponents();

		Timeline timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);

		scene = new Scene();

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (!scene.getActiveModels().isEmpty()) {
                try {
					for (ModifiedModel model : scene.getActiveModels()) {
						RenderEngine.render(canvas.getGraphicsContext2D(), camera, model.getTransformedModel(), (int) width, (int) height, Color.WHITE);
					}
                } catch (Exception e) {
					showExceptionNotification(e);
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
		canvas.requestFocus();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("objModels"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path filePath = Path.of(file.getAbsolutePath());
		String fileName = file.getName();

		try {
			ModifiedModel loadedModel = new ModifiedModel(ObjReader.read(Files.readString(filePath), false));
			scene.getModels().add(loadedModel);
		} catch (Exception e) {
			showExceptionNotification(e);
		}

		String tempName = fileName;
		int counter = 1;
		while (scene.getModelNames().contains(tempName)) {
			tempName = fileName;
			tempName = tempName + " (" + counter++ + ")";
		}

		listOfLoadedModelsNames.getItems().add(tempName);
		scene.getModelNames().add(tempName);
    }

	@FXML
	public void saveModel() {
		saveEditedModel();
	}

	@FXML
	public void selectModel() {
		List<ModifiedModel> selectedModels = new LinkedList<>();
		for (Integer index : listOfLoadedModelsNames.getSelectionModel().getSelectedIndices()) {
			selectedModels.add(scene.getModels().get(index));
		}
		scene.setActiveModels(selectedModels);
		canvas.requestFocus();
	}

	public void deleteModelFromViewList() {
		List<String> deletedModelNames = new LinkedList<>(listOfLoadedModelsNames.getSelectionModel().getSelectedItems());
		List<Integer> modelIndexesToRemove = new LinkedList<>(listOfLoadedModelsNames.getSelectionModel().getSelectedIndices());
		listOfLoadedModelsNames.setPrefHeight(listOfLoadedModelsNames.getHeight() - 28 * modelIndexesToRemove.size());
		listOfLoadedModelsNames.getItems().removeAll(listOfLoadedModelsNames.getSelectionModel().getSelectedItems());

		int decrement = 0;
		for (int index : modelIndexesToRemove) {
			scene.getModels().remove(index - decrement++);
		}

		scene.getActiveModels().removeIf(model -> ! scene.getModels().contains(model));
		scene.getModelNames().removeIf(deletedModelNames::contains);
		canvas.requestFocus();
	}
	public void saveInitialModel() {
		if (listOfLoadedModelsNames.getSelectionModel().getSelectedIndices().size() > 1) {
			showMessageNotification("Chose one Model to Save!");
		} else {
			saveModel(scene.getModels().get(listOfLoadedModelsNames.getSelectionModel().getSelectedIndex()));
		}
		canvas.requestFocus();
	}

	public void saveEditedModel() {
		if (listOfLoadedModelsNames.getSelectionModel().getSelectedIndices().size() > 1) {
			showMessageNotification("Chose one Model to Save!");
		} else {
			try {
				saveModel(scene.getModels().get(listOfLoadedModelsNames.getSelectionModel()
						.getSelectedIndex()).getTransformedModel());
			} catch (Exception e) {
				showExceptionNotification(e);
			}
		}
		canvas.requestFocus();
	}

	private void saveModel(Model modelToSave) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
		fileChooser.setTitle("Save Model");

		File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
		try {
			ObjWriter.writeToFile(modelToSave, file);
		} catch (IOException e) {
			showExceptionNotification(e);

		}
		showMessageNotification("File saved at:\n" + file.getAbsolutePath());
		canvas.requestFocus();
	}

	@FXML
    public void handleCameraForward() {
		moveCameraPosition(new Vector3f(0, 0, -TRANSLATION));
	}

    @FXML
    public void handleCameraBackward() {
		moveCameraPosition(new Vector3f(0, 0, TRANSLATION));
	}

    @FXML
    public void handleCameraLeft() {
		moveCameraPosition(new Vector3f(TRANSLATION, 0, 0));
	}

    @FXML
    public void handleCameraRight() {
		moveCameraPosition(new Vector3f(-TRANSLATION, 0, 0));
	}

    @FXML
    public void handleCameraUp() throws Exception {
		moveCameraPosition(new Vector3f(0, TRANSLATION, 0));
	}

    @FXML
    public void handleCameraDown() {
		moveCameraPosition(new Vector3f(0, -TRANSLATION, 0));
	}

	private void moveCameraPosition(Vector3f translationVector) {
		try {
			camera.moveCamera(translationVector);
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

	public void applyTransformation() {
		final float xT = spinnerMoveX.getValue().floatValue();
		final float yT = spinnerMoveY.getValue().floatValue();
		final float zT = spinnerMoveZ.getValue().floatValue();
		final Vector3f vT = new Vector3f(xT, yT, zT);

		final float xR = spinnerRotateX.getValue().floatValue();
		final float yR = spinnerRotateY.getValue().floatValue();
		final float zR = spinnerRotateZ.getValue().floatValue();
		final Vector3f vR = new Vector3f(xR, yR, zR);

		final float xS = spinnerScaleX.getValue().floatValue();
		final float yS = spinnerScaleY.getValue().floatValue();
		final float zS = spinnerScaleZ.getValue().floatValue();
		final Vector3f vS = new Vector3f(xS, yS, zS);
		try {
			Matrix4 modelMatrix = getModelMatrix(vT, vR, vS);
			for (ModifiedModel activeModel : scene.getActiveModels()) {
				activeModel.setRotateV(vR);
				activeModel.setScaleV(vS);
				activeModel.setTranslateV(vT);
			}
		} catch (Exception e) {
			showExceptionNotification(e);
		}
		canvas.requestFocus();
	}

	private void initFXMLComponents() {
		anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
		anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

		spinnerMoveX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100.0,100.0, 0.0, 0.5));
		spinnerMoveY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100.0,100.0, 0.0, 0.5));
		spinnerMoveZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100.0,100.0, 0.0, 0.5));

		spinnerScaleX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1,10, 1, 0.1));
		spinnerScaleY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1,10, 1, 0.1));
		spinnerScaleZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.1,10, 1, 0.1));

		spinnerRotateX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-360,360, 0.0, 1));
		spinnerRotateY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-360,360, 0.0, 1));
		spinnerRotateZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-360,360, 0.0, 1));

		modelsMenu = new Menu();
		listOfLoadedModelsNames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listOfLoadedModelsNames.setPrefHeight(0);
	}

	private void showExceptionNotification(Exception e) {
		Notifications.create()
				.text(e.getMessage())
				.position(Pos.CENTER)
				.showError();
	}

	private void showMessageNotification(String message) {
		Notifications.create()
				.text(message)
				.position(Pos.CENTER)
				.showError();
	}
}