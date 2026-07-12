package com.flowline.workflow_ui.ui;

import com.flowline.workflow_ui.client.WorkflowClient;
import com.flowline.workflow_ui.util.JsonBuilder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {

    private final WorkflowClient client = new WorkflowClient();

    public void show(Stage stage) {
        ComboBox<String> typeDropdown = new ComboBox<>();
        typeDropdown.getItems().addAll("wait", "http", "file");
        typeDropdown.setValue("wait");

        TextField nameField = new TextField();
        nameField.setPromptText("Step name");

        TextField valueField = new TextField();
        valueField.setPromptText("Value (seconds / url / file path)");

        Label resutLabel = new Label("Fill the form and click Run");
        Button button = new Button("Run Workflow");

        button.setOnAction(actionEvent -> {
            String type = typeDropdown.getValue();
            String name = nameField.getText();
            String value = valueField.getText();

            String json = JsonBuilder.buildSingleStepJson(type, name, value);
            resutLabel.setText(client.runWorkflow(json));
        });

        VBox root = new VBox(10, typeDropdown, nameField, valueField, button, resutLabel);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Flowline");
        stage.setScene(scene);
        stage.show();
    }

}
