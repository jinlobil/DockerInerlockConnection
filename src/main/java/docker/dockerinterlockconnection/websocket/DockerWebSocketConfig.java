package docker.dockerinterlockconnection.websocket;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Getter
public class DockerWebSocketConfig implements WebSocketConfigurer {
    private final DockerWebSocketHandler dockerWebSocketHandler;

    public DockerWebSocketConfig(DockerWebSocketHandler dockerWebSocketHandler) {
        this.dockerWebSocketHandler = dockerWebSocketHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this.dockerWebSocketHandler, "/docker/deploy").setAllowedOrigins("*");
    }
}
