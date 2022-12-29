package com.cgvsu;

import com.cgvsu.model.Model;
import com.cgvsu.objHandlers.ObjReader;
import com.cgvsu.objHandlers.ObjWriter;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.vecmath.Vector3f;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GuiController {

    final private float TRANSLATION = 0.5F;
	private Map<String, Model> loadedModels;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Model currentModel = null;
	private String currentModelName = null;

    private final Camera camera = new Camera(
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
		loadedModels = new HashMap<>();

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (currentModel != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, currentModel, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());
		currentModelName = file.getName();


		String fileContent;
		try {
			fileContent = Files.readString(fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		currentModel = ObjReader.read(fileContent, false);
    }

	@FXML
	public void saveModel() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
		fileChooser.setTitle("Save Model");
		
		File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
		try {
			ObjWriter.writeToFile(currentModel, file);
		} catch (IOException e) {
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