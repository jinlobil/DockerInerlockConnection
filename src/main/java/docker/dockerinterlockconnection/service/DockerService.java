package docker.dockerinterlockconnection.service;

import docker.dockerinterlockconnection.collector.DockerCollectorJob;
import docker.dockerinterlockconnection.dto.BasicAuthDto;
import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.dto.response.DockerWebSocketResponse;
import docker.dockerinterlockconnection.websocket.DockerWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class DockerService {
    private final DockerWebSocketHandler dockerWebSocketHandler;
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final DockerCollectorJob dockerCollectorJob;
    public boolean idValidationCheck(String id) {
        return id.matches("^[A-Za-z0-9:]+$");
    }

    public void notifyAlarm(boolean isSuccess, String message, DockerWebSocketResponse.DataType dataType) {
        this.dockerWebSocketHandler.broadcast(isSuccess, message, null, DockerWebSocketResponse.RequestType.ALARM, dataType);
    }

    private void notifyInfo(boolean isSuccess, String message, Object data, DockerWebSocketResponse.DataType dataType) {
        this.dockerWebSocketHandler.broadcast(isSuccess, message, data, DockerWebSocketResponse.RequestType.INFO, dataType);
    }

    public void notifyTotal(DockerResponseDto response,DockerWebSocketResponse.DataType dataType) {
        if (response.isSuccess()) {
            dockerCollectorJob.triggerCollect();
            Object cacheData = null;
            switch (dataType) {
                case IMAGE: cacheData = dockerCacheDataService.getImageCacheData();
                break;
                case VOLUME: cacheData = dockerCacheDataService.getVolumeCacheData();
                break;
                case CONTAINER:cacheData= dockerCacheDataService.getContainerCacheData();
                break;
            }
            notifyAlarm(true, response.getMessage(), dataType);
            notifyInfo(true,
                    response.getMessage(),
                    cacheData,
                    dataType
            );
        } else {
            notifyAlarm(false, response.getMessage(), dataType);
        }

    }
    public BasicAuthDto basicAuthDecode(String authorizationData) {
        if (authorizationData != null && authorizationData.startsWith("Basic ")) {
            // "Basic" 다음에 오는 부분이 베이직 인증 정보의 base64 인코딩된 문자열
            String base64Credentials = authorizationData.substring("Basic ".length()).trim();
            // Base64 디코딩
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":");

            String username = null;
            String password = null;
            if (parts.length == 2) {
                username = parts[0];
                password = parts[1];
                return new BasicAuthDto(true, username, password);
            }
        }
        return new BasicAuthDto(false, null, null);
    }
}
