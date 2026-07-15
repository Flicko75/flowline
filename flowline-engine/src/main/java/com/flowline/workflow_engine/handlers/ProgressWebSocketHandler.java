package com.flowline.workflow_engine.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class ProgressWebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession session;

    @Override
    public void afterConnectionEstablished(WebSocketSession newSession) {
        this.session = newSession;
    }

    public void sendUpdates(String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

}
