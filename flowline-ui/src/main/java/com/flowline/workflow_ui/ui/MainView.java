package com.flowline.workflow_ui.ui;

import com.flowline.workflow_ui.client.WorkflowClient;
import com.flowline.workflow_ui.models.StagedStep;
import com.flowline.workflow_ui.util.JsonBuilder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainView {

    private final WorkflowClient client = new WorkflowClient();

    private final List<StagedStep> stagedSteps = new ArrayList<>();

    private final ListView<String> stagedStepsListView = new ListView<>();

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
            stagedSteps.add(new StagedStep(
                    typeDropdown.getValue(),
                    nameField.getText(),
                    valueField.getText()
            ));

            stagedStepsListView.getItems().add(
                    typeDropdown.getValue() + " - " + nameField.getText() + " - " + valueField.getText()
            );

            typeDropdown.setValue(null);
            nameField.clear();
            valueField.clear();
        });

        runButton.setOnAction(actionEvent -> {
            if (stagedSteps.isEmpty()) {
                resultLabel.setText("Add at least one step first");
                return;
            }

            String json = JsonBuilder.buildStepsJson(stagedSteps);
            resultLabel.setText(client.runWorkflow(json));
        });

        VBox root = new VBox(10, typeDropdown, nameField, valueField, addStepButton, stagedStepsListView, runButton, resultLabel);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Flowline");
        stage.setScene(scene);
        stage.show();
    }

}
