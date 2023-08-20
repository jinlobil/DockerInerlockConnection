package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.*;
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
    private final DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DockerResponseDto getContainerList(){
        Object containerCacheData= this.dockerCacheDataService.getContainerCacheData();
        if (containerCacheData == null){
            return new DockerResponseDto(false,"Container inquiry failed",null);
        }
        return new DockerResponseDto(true,"Container inquiry completed",containerCacheData);
    }

    public DockerResponseDto getContainerInspect(String containerId){
        if (!dockerService.idValidationCheck(containerId)) {
            return new DockerResponseDto(false, "ContainerId is invalid", null);
        }
        if (containerId == null){
            return new DockerResponseDto(false, "ContainerId is null", null);
        }
        CommandExecuteResponse response = dockerCommandUtil.execute("docker container inspect " + containerId + " --format json");

        if (response.isSuccess()){
            List<ContainerInspectDto> inspectDtoList = null;
            try {
                inspectDtoList = this.objectMapper.readValue(response.getData(), new TypeReference<List<ContainerInspectDto>>() {
                });
            } catch (JsonProcessingException e) {
                log.error("ContainerService_getContainerInspect Json Parsing Error {}", e);
            }
            if (inspectDtoList.size() == 0) {
                return new DockerResponseDto(false, "Container inquiry failed", null);
            }
            return new DockerResponseDto(true,"Container inquiry completed", inspectDtoList.get(0));
        }
        return new DockerResponseDto(false,"Container inquiry failed",null);
    }

    public DockerResponseDto createContainer(ContainerRequestDto containerData){
        if (containerData.getContainerName() == null){
            return new DockerResponseDto(false, "ContainerName is null", null);
        }
        if (containerData.getImageId() == null) {
            return new DockerResponseDto(false, "ImageId is null", null);
        }
        if (!dockerService.idValidationCheck(containerData.getImageId())) {
            return new DockerResponseDto(false, "ImageId is invalid", null);
        }
        CommandExecuteResponse response = dockerCommandUtil.execute(createContainerCmd(containerData).toString());
        if (!response.isSuccess()) {
            return new DockerResponseDto(false, response.getData(), null);
        }
        return new DockerResponseDto(true, "Container created completed", response.getData());
    }

    public DockerResponseDto deleteContainer(String containerId) {
        if (!dockerService.idValidationCheck(containerId)) {
            return new DockerResponseDto(false, "ContainerId is invalid", null);
        }
        CommandExecuteResponse response = dockerCommandUtil.execute("docker container rm -f " + containerId);
        if (!response.isSuccess()) {
            return new DockerResponseDto(false, response.getData(), null);
        }
        return new DockerResponseDto(true, "Container deleted completed", response.getData());
    }

    public StringBuilder createContainerCmd(ContainerRequestDto containerData){
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
        return sb;
    }

    public List<String> getContainerIdList() {
        CommandExecuteResponse result = dockerCommandUtil.execute("docker ps -a | awk '{ print $1 }'");
        if (result.isSuccess()) {
            String[] array= result.getData().replace(")","").replace("CONTAINER","").trim().split("\\n");
            return Arrays.stream(array)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
