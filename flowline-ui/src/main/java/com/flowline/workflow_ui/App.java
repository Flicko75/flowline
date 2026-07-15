package com.flowline.workflow_ui;

import com.flowline.workflow_ui.client.ProgressWebsocketClient;
import com.flowline.workflow_ui.ui.MainView;
import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage stage) {
        new ProgressWebsocketClient().connect();
        new MainView().show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
