package docker.dockerinterlockconnection.service;

import docker.dockerinterlockconnection.collector.DockerCollectorJob;
import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.dto.response.DockerWebSocketResponse;
import docker.dockerinterlockconnection.websocket.DockerWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
