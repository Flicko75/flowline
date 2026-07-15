package com.flowline.workflow_ui.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class ProgressWebsocketClient {

    private WebSocket webSocket;

    public void connect() {
        HttpClient client = HttpClient.newHttpClient();

        client.newWebSocketBuilder()
                .buildAsync(URI.create("ws://localhost:8080/progress"),
                        new WebSocket.Listener() {
                            @Override
                            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                                System.out.println("Received: " + data);
                                webSocket.request(1);
                                return null;
                            }
                        }
                )
                .thenAccept(ws -> this.webSocket = ws);
    }

}
