package com.flowline.workflow_ui.client;

import com.flowline.workflow_ui.ui.MainView;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class ProgressWebsocketClient {

    private WebSocket webSocket;

    private final MainView mainView;

    public ProgressWebsocketClient(MainView mainView) {
        this.mainView = mainView;
    }

    public void connect() {
        HttpClient client = HttpClient.newHttpClient();

        client.newWebSocketBuilder()
                .buildAsync(URI.create("ws://localhost:8080/progress"),
                        new WebSocket.Listener() {
                            @Override
                            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                                String message = data.toString();
                                System.out.println("Raw message: " + message);

                                try {
                                    String stepName = message.split("\"stepName\":\"")[1].split("\"")[0];
                                    String status = message.split("\"status\":\"")[1].split("\"")[0];

                                    System.out.println("Parsed stepName=" + stepName + " status=" + status);

                                    mainView.updateStepStatus(stepName, status);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                webSocket.request(1);
                                return null;
                            }
                        }
                )
                .thenAccept(ws -> this.webSocket = ws);
    }

}
