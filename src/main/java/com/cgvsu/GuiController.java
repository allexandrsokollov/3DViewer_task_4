package com.cgvsu;

import com.cgvsu.math.Matrix4;
import com.cgvsu.model.Model;
import com.cgvsu.objHandlers.ObjReader;
import com.cgvsu.objHandlers.ObjWriter;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.math.Vector3f;

import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

import static com.cgvsu.render_engine.GraphicConveyor.getModelMatrix;

public class GuiController {

    final private float TRANSLATION = 0.5F;
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

	private Map<String, Model> editedLoadedModels;
	private Map<String, Model> initialLoadedModels;

	private Map<String, Model> activeModels;

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

		Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
		editedLoadedModels = new HashMap<>();
		initialLoadedModels = new HashMap<>();
		activeModels = new HashMap<>();

		spinnerMoveX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100.0,100.0, 0.0, 0.5));
		spinnerMoveY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100.0,100.0, 0.0, 0.5));
		spinnerMoveZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100.0,100.0, 0.0, 0.5));

		spinnerScaleX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-10,10, 0.0, 0.1));
		spinnerScaleY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-10,10, 0.0, 0.1));
		spinnerScaleZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-10,10, 0.0, 0.1));

		spinnerRotateX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-360,360, 0.0, 1));
		spinnerRotateY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-360,360, 0.0, 1));
		spinnerRotateZ.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-360,360, 0.0, 1));

		modelsMenu = new Menu();

		listOfLoadedModelsNames.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (currentModel != null) {
                try {
					RenderEngine.render(canvas.getGraphicsContext2D(), camera, currentModel, (int) width, (int) height);
                } catch (Exception e) {
					showExceptionNotification(e);
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
			currentModel = ObjReader.read(fileContent, false);
			editedLoadedModels.put(currentModelName, currentModel);
			initialLoadedModels.put(currentModelName, currentModel.getCopy());
		} catch (IOException e) {
			showExceptionNotification(e);
		}
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

		//activeModels.put(modelName, newModel);
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
			showExceptionNotification(e);

		}
		Notifications.create()
				.text("File saved at:\n" + file.getAbsolutePath())
				.position(Pos.CENTER)
				.showInformation();
	}

	public void moveXCoordinate(KeyEvent keyEvent) {
		moveModel(new Vector3f(spinnerMoveX.getValue().floatValue(), 0, 0), keyEvent);
	}

	public void moveYCoordinate(KeyEvent keyEvent) {
		moveModel(new Vector3f(0, spinnerMoveY.getValue().floatValue(), 0), keyEvent);
	}

	public void moveZCoordinate(KeyEvent keyEvent) {
		moveModel(new Vector3f(0, 0, spinnerMoveZ.getValue().floatValue()), keyEvent);
	}

	private void moveModel(Vector3f shiftVector, KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			try {
				Matrix4 modelMatrix = getModelMatrix(shiftVector,
						new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
				currentModel.makeTransformation(modelMatrix);
			} catch (Exception e) {
				showExceptionNotification(e);
			}
		}
		canvas.requestFocus();
	}

	public void scaleZ(KeyEvent keyEvent) {
		scaleModel(new Vector3f(1, 1, spinnerScaleZ.getValue().floatValue()), keyEvent);
	}

	public void scaleY(KeyEvent keyEvent) {
		scaleModel(new Vector3f(1, spinnerScaleY.getValue().floatValue(), 1), keyEvent);
	}

	public void scaleX(KeyEvent keyEvent) {
		scaleModel(new Vector3f(spinnerScaleX.getValue().floatValue(), 1, 1), keyEvent);
	}

	private void scaleModel(Vector3f scaleVector, KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ENTER)) {
			try {
				Matrix4 modelMatrix = getModelMatrix(new Vector3f(0, 0, 0),
						new Vector3f(0, 0, 0), scaleVector);
				currentModel.makeTransformation(modelMatrix);
			} catch (Exception e) {
				showExceptionNotification(e);
			}
		}
		canvas.requestFocus();
	}

	public void rotateZ(KeyEvent keyEvent) {
		rotateModel(new Vector3f(0, 0, spinnerRotateZ.getValue().floatValue()), keyEvent);
	}

	public void rotateY(KeyEvent keyEvent) {
		rotateModel(new Vector3f(0, spinnerRotateY.getValue().floatValue(), 0), keyEvent);
	}

	public void rotateX(KeyEvent keyEvent) {
		rotateModel(new Vector3f(spinnerRotateX.getValue().floatValue(), 0, 0), keyEvent);
	}

	private void rotateModel(Vector3f rotateVector, KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.ENTER)) {
			try {
				Matrix4 modelMatrix = getModelMatrix(new Vector3f(0, 0, 0),
						rotateVector, new Vector3f(1, 1, 1));
				currentModel.makeTransformation(modelMatrix);
			} catch (Exception e) {
				showExceptionNotification(e);
			}
		}
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
    public void handleCameraUp() {
		moveCameraPosition(new Vector3f(0, TRANSLATION, 0));
	}

    @FXML
    public void handleCameraDown() {
		moveCameraPosition(new Vector3f(0, -TRANSLATION, 0));
	}

	public void  rotateCamera(MouseEvent event) {

	}

	private void moveCameraPosition(Vector3f translationVector) {
		try {
			camera.moveCamera(translationVector);
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

	private void showExceptionNotification(Exception e) {
		Notifications.create()
				.text(e.getMessage())
				.position(Pos.CENTER)
				.showError();
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
			currentModel.makeTransformation(modelMatrix);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}
}