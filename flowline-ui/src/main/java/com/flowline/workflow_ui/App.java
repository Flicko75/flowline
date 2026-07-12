package com.flowline.workflow_ui;

import com.flowline.workflow_ui.client.WorkflowClient;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App extends Application {

    private final WorkflowClient client = new WorkflowClient();

    @Override
    public void start(Stage stage) {
        Label resultLabel = new Label("Click the button to run a workflow");
        Button button = new Button("Run Workflow");

        button.setOnAction(event -> {
            String json = "[{\"type\":\"wait\",\"name\":\"step1\",\"seconds\":2}]";
            resultLabel.setText(client.runWorkflow(json));
        });

        VBox root = new VBox(15, button, resultLabel);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Flowline");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
