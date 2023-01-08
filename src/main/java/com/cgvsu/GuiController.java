package com.cgvsu;

import com.cgvsu.math.Vector2f;
import com.cgvsu.model.Model;
import com.cgvsu.model.ModifiedModel;
import com.cgvsu.objHandlers.ObjReader;
import com.cgvsu.objHandlers.ObjWriter;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.math.Vector3f;

import com.cgvsu.render_engine.CameraController;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.Scene;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import static javax.imageio.ImageIO.read;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.LinkedList;

public class GuiController {

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
	public RadioButton radioButtonMesh;
	@FXML
	public RadioButton radioButtonTexture;
	@FXML
	public RadioButton radioButtonShades;
	@FXML
	public RadioButton radioButtonSolidColor;
	@FXML
	public ColorPicker modelColor;
	@FXML
	public ListView<String> cameraNamesList;
	@FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
	private Scene scene;
	private boolean isRotationActive;
	private final Vector2f currentMouseCoordinates = new Vector2f(0, 0);
	private final Vector2f centerCoordinates = new Vector2f(0 , 0);
	private final float TRANSLATION = 0.8F;

	@FXML
    private void initialize() {
		initFXMLComponents();

		Timeline timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);

		scene = new Scene();

		scene.getCameraControllers().add(new CameraController(new Camera(
				new Vector3f(0, 0, 100),
				new Vector3f(0, 0, 0),
				1.0F, 1, 0.01F, 100), TRANSLATION));
		scene.setCurrentCameraController(scene.getCameraControllers().get(0));

		cameraNamesList.getItems().add("Camera №0");
		scene.getCameraNames().add("Camera №0");

		KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
			double width = canvas.getWidth();
			double height = canvas.getHeight();

			canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
			scene.getCurrentCameraController().getCamera().setAspectRatio((float) (width / height));


			if (isRotationActive) {
				rotateCamera();
			}

			if (!scene.getActiveModels().isEmpty()) {
				try {
					for (ModifiedModel model : scene.getActiveModels()) {
						RenderEngine.render(canvas.getGraphicsContext2D(), scene.getCurrentCameraController().getCamera(),
								model.getTransformedModel(), (int) width, (int) height,
								modelColor.getValue(), model.getTexture(), getRenderWayData());
					}
				} catch (Exception e) {
					showExceptionNotification(e);
				}
			}
		});

        timeline.getKeyFrames().add(frame);
        timeline.play();

		canvas.getOnMouseMoved();
		canvas.requestFocus();
    }

	/**
	 * @return four boolean values
	 * 1. is draw mesh or not
	 * 2. is draw shades or not
	 * 3. is draw texture or not
	 * 4. is draw solid color or not
	 */
	public boolean[] getRenderWayData() {
		return new boolean[] {radioButtonMesh.isSelected(), radioButtonShades.isSelected(),
				radioButtonTexture.isSelected(), radioButtonSolidColor.isSelected()};
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
	public void selectModel() {
		var selectedModels = new LinkedList<ModifiedModel>();
		for (Integer index : listOfLoadedModelsNames.getSelectionModel().getSelectedIndices()) {
			selectedModels.add(scene.getModels().get(index));
		}
		scene.setActiveModels(selectedModels);
		canvas.requestFocus();
	}

	@FXML
	public void deleteModelFromViewList() {
		scene.deleteSelectedModels(new LinkedList<>(listOfLoadedModelsNames.getSelectionModel().getSelectedIndices()),
				new LinkedList<>(listOfLoadedModelsNames.getSelectionModel().getSelectedItems()));

		listOfLoadedModelsNames.getItems().removeAll(listOfLoadedModelsNames.getSelectionModel().getSelectedItems());
		canvas.requestFocus();
	}
	@FXML
	public void saveInitialModel() {
		if (listOfLoadedModelsNames.getSelectionModel().getSelectedIndices().size() > 1) {
			showMessageNotification("Chose one Model to Save!");
		} else {
			saveModel(scene.getModels().get(listOfLoadedModelsNames.getSelectionModel().getSelectedIndex()).getMesh());
		}
		canvas.requestFocus();
	}
	@FXML
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
		try {
			scene.getCurrentCameraController().handleCameraForward();
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

    @FXML
    public void handleCameraBackward() {
		try {
			scene.getCurrentCameraController().handleCameraBackward();
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

    @FXML
    public void handleCameraLeft() {
		try {
			scene.getCurrentCameraController().handleCameraLeft();
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

    @FXML
    public void handleCameraRight() {
		try {
			scene.getCurrentCameraController().handleCameraRight();
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

    @FXML
    public void handleCameraUp() {
		try {
			scene.getCurrentCameraController().handleCameraUp();
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

    @FXML
    public void handleCameraDown() {
		try {
			scene.getCurrentCameraController().handleCameraDown();
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

	@FXML
	public void applyTransformation() {
		try {
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
				for (ModifiedModel activeModel : scene.getActiveModels()) {
					activeModel.setRotateV(vR);
					activeModel.setScaleV(vS);
					activeModel.setTranslateV(vT);
				}
			} catch (Exception e) {
				showExceptionNotification(e);
			}

		} catch (NullPointerException e) {
			showMessageNotification("One of fields is empty");
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
				.showInformation();
	}

	@FXML
	public void takeFocusCanvas() {
		canvas.requestFocus();
	}

	@FXML
	public void canvasDragDroppedGetValue() {
		isRotationActive = false;
	}

	@FXML
	public void canvasDragEnterGetValue() {
		isRotationActive = true;
	}

	public void rotateCamera() {
		centerCoordinates.setX((float) (canvas.getWidth() / 2));
		centerCoordinates.setY((float) (canvas.getHeight() / 2));

		float diffX = currentMouseCoordinates.getX() - centerCoordinates.getX();
		float diffY = currentMouseCoordinates.getY() - centerCoordinates.getY();

		float xAngle = (float) ((diffX / canvas.getWidth()) * 1);
		float yAngle = (float) ((diffY / canvas.getHeight()) * -1);

		try {
			scene.getCurrentCameraController().rotateCamera(new Vector2f(xAngle, yAngle));
		} catch (Exception e) {
			showExceptionNotification(e);
		}
	}

	@FXML
	public void currentMouseCoordinates(MouseEvent mouseDragEvent) {
		currentMouseCoordinates.setX( (float) mouseDragEvent.getX());
		currentMouseCoordinates.setY( (float) mouseDragEvent.getY());
	}

	public void loadTexture() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texture (*.png)", "*.png", "*.jpg"));
		fileChooser.setTitle("Load Texture");

		File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());

		if (scene.getActiveModels().size() > 1) {
			showMessageNotification("select one model to attach texture");
		} else {
			try {
				BufferedImage texture = read(file);
				scene.getActiveModels().get(0).setTexture(texture);
			} catch (IOException e) {
				showExceptionNotification(e);
			}
		}
	}

	@FXML
	public void addCamera() {
		scene.getCameraControllers().add(new CameraController(new Camera(
				new Vector3f(0, 0, 100),
				new Vector3f(0, 0, 0),
				1.0F, 1, 0.01F, 100), TRANSLATION));

		int counter = 0;
		String tempName = "Camera №" + counter;
		while (scene.getCameraNames().contains(tempName)) {
			tempName = "Camera №";
			tempName = tempName + counter++;
		}

		scene.getCameraNames().add(tempName);
		cameraNamesList.getItems().add(tempName);
	}

	@FXML
	public void changeCurrentCamera() {
		scene.setCurrentCameraController(
				scene.getCameraControllers().get(
						cameraNamesList.getSelectionModel().getSelectedIndex()));
	}

	public void deleteCamera() {
		if (scene.getCameraControllers().size() <= 1) {
			showMessageNotification("You can not delete last camera!");
		} else {
			scene.deleteCamera(
					cameraNamesList.getSelectionModel().getSelectedIndex(),
					cameraNamesList.getSelectionModel().getSelectedItem());
			cameraNamesList.getItems().remove(cameraNamesList.getSelectionModel().getSelectedIndex());
		}
	}
}