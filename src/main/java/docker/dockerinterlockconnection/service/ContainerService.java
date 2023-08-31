package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.collector.DockerCollectorJob;
import docker.dockerinterlockconnection.dto.*;
import docker.dockerinterlockconnection.dto.request.ContainerRequestDto;
import docker.dockerinterlockconnection.dto.response.CommandExecuteResponse;
import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.dto.response.DockerWebSocketResponse;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContainerService {
    private final DockerService dockerService;
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final DockerCollectorJob dockerCollectorJob;
    private final DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DockerResponseDto getContainerList(){
        Object containerCacheData= this.dockerCacheDataService.getContainerCacheData();
        if (containerCacheData == null){
            return new DockerResponseDto(false,"Container inquiry failed",null);
        }
        return new DockerResponseDto(true,"Container inquiry completed",containerCacheData);
    }

    public DockerResponseDto statusControl(ContainerRequestDto containerData) {
        if (containerData.getContainerId() == null) {
            dockerService.notifyAlarm(false, "ContainerId is null", DockerWebSocketResponse.DataType.CONTAINER);
            return new DockerResponseDto(false, "ContainerId is null", null);
        }
        if (containerData.getStatus() == null) {
            dockerService.notifyAlarm(false, "Status is null", DockerWebSocketResponse.DataType.CONTAINER);
            return new DockerResponseDto(false, "Status is null", null);
        }
        String status = null;
        switch (containerData.getStatus()) {
            case 0: status = "start";
                break;
            case 1: status = "stop";
                break;
            case 2:status= "restart";
                break;
        }

        CommandExecuteResponse response = dockerCommandUtil.execute("docker " + status + " " + containerData.getContainerId());
        DockerResponseDto dockerResponseDto = null;
        if (response.isSuccess()){
            dockerCollectorJob.triggerCollect();
            dockerResponseDto = new DockerResponseDto(true, "Container status completed", response.getData());

        }else {
            dockerResponseDto = new DockerResponseDto(false, response.getData(), null);
        }
        dockerService.notifyTotal(dockerResponseDto, DockerWebSocketResponse.DataType.CONTAINER);
        return dockerResponseDto;
    }

    public DockerResponseDto createContainer(ContainerRequestDto containerData){
        if (containerData.getContainerName() == null){
            dockerService.notifyAlarm(false, "ContainerName is null", DockerWebSocketResponse.DataType.CONTAINER);
            return new DockerResponseDto(false, "ContainerName is null", null);
        }
        if (containerData.getImageId() == null) {
            dockerService.notifyAlarm(false, "ImageId is null", DockerWebSocketResponse.DataType.CONTAINER);
            return new DockerResponseDto(false, "ImageId is null", null);
        }
        if (!dockerService.idValidationCheck(containerData.getImageId())) {
            dockerService.notifyAlarm(false, "ImageId is invalid", DockerWebSocketResponse.DataType.CONTAINER);
            return new DockerResponseDto(false, "ImageId is invalid", null);
        }

        CommandExecuteResponse response = dockerCommandUtil.execute(createContainerCmd(containerData).toString());
        DockerResponseDto dockerResponseDto = null;
        if (!response.isSuccess()) {
            dockerResponseDto = new DockerResponseDto(false, response.getData(), null);
        }else {
            dockerCollectorJob.triggerCollect();
            dockerResponseDto = new DockerResponseDto(true, "Container created completed", response.getData());
        }
        dockerService.notifyTotal(dockerResponseDto, DockerWebSocketResponse.DataType.CONTAINER);
        return dockerResponseDto;
    }

    public DockerResponseDto deleteContainer(String containerId) {
        if (!dockerService.idValidationCheck(containerId)) {
            dockerService.notifyAlarm(false, "ContainerId is invalid", DockerWebSocketResponse.DataType.CONTAINER);
            return new DockerResponseDto(false, "ContainerId is invalid", null);
        }

        CommandExecuteResponse response = dockerCommandUtil.execute("docker container rm -f " + containerId);
        DockerResponseDto dockerResponseDto = null;
        if (!response.isSuccess()) {
            dockerResponseDto = new DockerResponseDto(false, response.getData(), null);
        }else {
            dockerCollectorJob.triggerCollect();
            dockerResponseDto = new DockerResponseDto(true, "Container deleted completed", response.getData());
        }
        dockerService.notifyTotal(dockerResponseDto, DockerWebSocketResponse.DataType.CONTAINER);
        return dockerResponseDto;
    }

    private StringBuilder createContainerCmd(ContainerRequestDto containerData){
        StringBuilder sb = new StringBuilder();
        sb.append("docker container run -d");
        sb.append(" ").append("--name").append(" ").append(containerData.getContainerName());
        if (containerData.getPublishList() != null && !containerData.getPublishList().isEmpty()) {
            for (String publish : containerData.getPublishList()) {
                sb.append(" -p ").append(publish);
            }
        }
        if (containerData.getMountList() != null && !containerData.getMountList().isEmpty()) {
            for (MountDto Mount : containerData.getMountList()) {
                if (Mount.getMountType() == null) {
                    continue;
                }
                sb.append(" --mount");
                String volumeType = null;
                if (Mount.getMountType() != null) {
                    switch (Mount.getMountType()) {
                        case 0:
                            volumeType = "volume";
                            break;
                        case 1:
                            volumeType = "bind";
                            break;
                        case 2:
                            volumeType = "tmpfs";
                            break;
                    }
                    sb.append(" type=").append(volumeType);
                }
                if (Mount.getMountSource() != null) {
                    sb.append(",source=").append(Mount.getMountSource());
                }
                if (volumeType == null || volumeType.equals("tmpfs")){
                    sb.append(",destination=").append(Mount.getMountTarget());
                }
//                if (Mount.getMountDestination() != null) {
//                    sb.append(",destination=").append(Mount.getMountDestination());
//                }
                if (Mount.getMountTarget() != null) {
                    sb.append(",target=").append(Mount.getMountTarget());
                }
//                if (Mount.getMountReadOnly()) {
//                    sb.append(",readonly");
//                }
            }
        }
        if (containerData.getEnv() != null) {
            Map<String, String> envMap = containerData.getEnv();
            for (Map.Entry<String, String> keyAndValue : envMap.entrySet()) {
                sb.append(" -e");
                sb.append(" ").append(keyAndValue.getKey()).append("=").append(keyAndValue.getValue());
            }
        }
        return sb.append(" ").append(containerData.getImageId());
    }


}
