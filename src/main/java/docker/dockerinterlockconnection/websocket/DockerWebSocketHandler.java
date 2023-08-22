package docker.dockerinterlockconnection.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class DockerWebSocketHandler extends TextWebSocketHandler {
    protected final Map<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Client connected, ID: {}, RemoteAddress: {}", session.getId(), session.getRemoteAddress());
        this.webSocketSessions.put(session.getId(), session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Client disconnected, ID: {}", session.getId());
        this.webSocketSessions.remove(session.getId());
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("Transport error, ID: {}, error: {}", session.getId(), exception.getMessage());
        exception.printStackTrace();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.debug("Client got a message, ID: {}, Payload: {}", session.getId(), message.getPayload());
    }

    protected void broadcast(String packet) {
        TextMessage message = new TextMessage(packet);
        log.info("broadcast(): Session size is {}", this.webSocketSessions.size());
        if (this.webSocketSessions.size() > 0) {
            for (WebSocketSession session : this.webSocketSessions.values()) {
                try {
                    log.info("!!!!!!!!!!!!!!! {}: {}", session.getId(), message);
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.debug("broadcast(): No session");
        }
    }
}
