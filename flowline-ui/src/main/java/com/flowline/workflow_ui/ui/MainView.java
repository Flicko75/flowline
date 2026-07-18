package com.flowline.workflow_ui.ui;

import com.flowline.workflow_ui.client.WorkflowClient;
import com.flowline.workflow_ui.models.StagedStep;
import com.flowline.workflow_ui.util.JsonBuilder;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainView {

    private final WorkflowClient client = new WorkflowClient();

    private final List<StagedStep> stagedSteps = new ArrayList<>();

    private final Map<String, Label> statusLabels = new HashMap<>();

    private final HBox stepsContainer = new HBox(10);

    public void show(Stage stage) {
        ComboBox<String> typeDropdown = new ComboBox<>();
        typeDropdown.getItems().addAll("wait", "http", "file");
        typeDropdown.setPromptText("Select type");

        TextField nameField = new TextField();
        nameField.setPromptText("Step name");

        TextField valueField = new TextField();
        valueField.setPromptText("Value (seconds / url / file path)");

        Label resultLabel = new Label("Fill the form and click Run");
        Button addStepButton = new Button("Add Step");
        Button runButton = new Button("Run Workflow");

        addStepButton.setOnAction(actionEvent -> {
            if (typeDropdown.getValue() == null || nameField.getText().isBlank() || valueField.getText().isBlank()) {
                resultLabel.setText("Fill all fields before adding a step");
                return;
            }

            StagedStep stagedStep = new StagedStep(
                    typeDropdown.getValue(),
                    nameField.getText(),
                    valueField.getText()
            );

            stagedSteps.add(stagedStep);
            stepsContainer.getChildren().add(createStepBox(stagedStep));

            typeDropdown.setValue(null);
            nameField.clear();
            valueField.clear();
        });

        runButton.setOnAction(actionEvent -> {
            if (stagedSteps.isEmpty()) {
                resultLabel.setText("Add at least one step first");
                return;
            }

            Task<String> runTask = new Task<>() {
                @Override
                protected String call() {
                    String json = JsonBuilder.buildStepsJson(stagedSteps);
                    return client.runWorkflow(json);
                }
            };

            runTask.setOnSucceeded(event -> resultLabel.setText(runTask.getValue()));

            new Thread(runTask).start();
        });

        VBox root = new VBox(10, typeDropdown, nameField, valueField, addStepButton, stepsContainer, runButton, resultLabel);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Flowline");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createStepBox(StagedStep step) {
        Label nameLabel = new Label(step.name());
        Label typeLabel = new Label(step.type());
        Label statusLabel = new Label("NOT_STARTED");

        VBox box = new VBox(5, nameLabel, typeLabel, statusLabel);
        box.setStyle("-fx-background-color: grey; -fx-padding: 10; -fx-border-color: black;");
        box.setPrefWidth(100);

        statusLabels.put(step.name(), statusLabel);

        return box;
    }

    public void updateStepStatus(String stepName, String status) {
        Platform.runLater(() -> {
            Label statusLabel = statusLabels.get(stepName);
            if (statusLabel != null) {
                statusLabel.setText(status);

                VBox box = (VBox) statusLabel.getParent();
                String color = switch (status) {
                    case "RUNNING" -> "yellow";
                    case "COMPLETED" -> "lightgreen";
                    case "FAILED" -> "salmon";
                    default -> "grey";
                };

                box.setStyle("-fx-background-color: " + color + "; -fx-padding: 10; -fx-border-color: black;");
            }
        });
    }

}
