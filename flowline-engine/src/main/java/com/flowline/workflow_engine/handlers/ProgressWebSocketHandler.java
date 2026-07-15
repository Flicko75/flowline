package com.flowline.workflow_engine.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowline.workflow_engine.engine.models.ProgressMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ProgressWebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession session;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession newSession) {
        this.session = newSession;
    }

    public void sendUpdate(ProgressMessage message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        session.sendMessage(new TextMessage(json));
    }

}
