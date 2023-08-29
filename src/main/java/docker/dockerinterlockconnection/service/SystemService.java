package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.*;
import docker.dockerinterlockconnection.dto.response.CommandExecuteResponse;
import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SystemService {
    DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final ObjectMapper objectMapper=  new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public DockerResponseDto getSystemDataUsing(){
        CommandExecuteResponse response = dockerCommandUtil.execute("docker system df -v --format json");
        if (response.isSuccess()){
            try {
                return new DockerResponseDto(true,"System inquiry completed", this.objectMapper.readValue(response.getData(), SystemDto.class));
            } catch (JsonProcessingException e) {
                log.error("SystemService_getSystemDataUsing Json Parsing Error {}", e);
            }
        }
        return new DockerResponseDto(false,"System inquiry failed",null);
    }

    public DockerResponseDto getSystemInfo() {
        CommandExecuteResponse response = dockerCommandUtil.execute("docker info --format json");
        if (response.isSuccess()) {
            try {
                return new DockerResponseDto(true, "System info inquiry completed", this.objectMapper.readValue(response.getData(), SystemInfoDto.class));
            } catch (JsonProcessingException e) {
                log.error("SystemService_getSystemInfo Json Parsing Error {}", e);
            }
        }
        return new DockerResponseDto(false, "System info inquiry failed", null);
    }
    public DockerResponseDto getSystemInfoData() {
        Object SystemInfoCacheData= this.dockerCacheDataService.getSystemInfoCacheData();
        if (SystemInfoCacheData == null){
            return new DockerResponseDto(false,"SystemInfo inquiry failed",null);
        }
        return new DockerResponseDto(true,"SystemInfo inquiry completed",SystemInfoCacheData);
    }

    public boolean idValidationCheck(String id) {
        return id.matches("^[A-Za-z0-9:]+$");
    }

    public DockerResponseDto getContainerInspect(String containerId) {
        if (!this.idValidationCheck(containerId)) {
            return new DockerResponseDto(false, "ContainerId is invalid", (Object)null);
        } else if (containerId == null) {
            return new DockerResponseDto(false, "ContainerId is null", (Object)null);
        } else {
            CommandExecuteResponse response = this.dockerCommandUtil.execute("docker container inspect " + containerId + " --format json");
            if (response.isSuccess()) {
                List<ContainerInspectDto> inspectDtoList = null;

                try {
                    inspectDtoList = this.objectMapper.readValue(response.getData(), new TypeReference<List<ContainerInspectDto>>() {
                    });
                } catch (JsonProcessingException var5) {
                    log.error("ContainerService_getContainerInspect Json Parsing Error {}", var5);
                }

                return inspectDtoList.size() == 0 ? new DockerResponseDto(false, "Container inquiry failed", (Object)null) : new DockerResponseDto(true, "Container inquiry completed", inspectDtoList.get(0));
            } else {
                return new DockerResponseDto(false, "Container inquiry failed", (Object)null);
            }
        }
    }

    public DockerResponseDto getLocalImageInspect(String imageId) {
        if (!this.idValidationCheck(imageId)) {
            return new DockerResponseDto(false, "imageId is invalid", (Object)null);
        } else {
            String cmd = "docker image inspect " + imageId + " --format json";
            CommandExecuteResponse response = this.dockerCommandUtil.execute(cmd);
            if (response.isSuccess()) {
                List<ImageInspectDto> inspectDtoList = null;

                try {
                    inspectDtoList = this.objectMapper.readValue(response.getData(), new TypeReference<List<ImageInspectDto>>() {
                    });
                } catch (JsonProcessingException var6) {
                    log.error("ImageService_getLocalImageInspect Json Parsing Error {}", var6);
                }

                return inspectDtoList.size() == 0 ? new DockerResponseDto(false, "Image inquiry failed", (Object)null) : new DockerResponseDto(true, "Image inquiry completed", inspectDtoList.get(0));
            } else {
                return new DockerResponseDto(false, "Image inquiry failed", (Object)null);
            }
        }
    }

    public DockerResponseDto getVolumeInspect(String volumeId) {
        if (!this.idValidationCheck(volumeId)) {
            return new DockerResponseDto(false, "volumeId is invalid", (Object)null);
        } else if (volumeId == null) {
            return new DockerResponseDto(false, "volumeId is null", (Object)null);
        } else {
            CommandExecuteResponse response = this.dockerCommandUtil.execute("docker volume inspect " + volumeId + " --format json");
            if (response.isSuccess()) {
                List<VolumeInspectDto> inspectDtoList = null;

                try {
                    inspectDtoList = this.objectMapper.readValue(response.getData(), new TypeReference<List<VolumeInspectDto>>() {
                    });
                } catch (JsonProcessingException var5) {
                    log.error("VolumeService_getVolumeInspect Json Parsing Error {}", var5);
                }

                if (inspectDtoList.size() == 0) {
                    return new DockerResponseDto(false, "Volume inquiry failed", (Object)null);
                } else {
                    ((VolumeInspectDto)inspectDtoList.get(0)).setUseContainerNames(this.getVolumeUseContainerName(((VolumeInspectDto)inspectDtoList.get(0)).getVolumeName()));
                    return new DockerResponseDto(true, "Volume inquiry completed", inspectDtoList.get(0));
                }
            } else {
                return new DockerResponseDto(false, "Volume inquiry failed", (Object)null);
            }
        }
    }

    private List<String> getVolumeUseContainerName(String volumeId) {
        List<String> containerNameList = new ArrayList<>();
        for (String containerId : getContainerIdList()) {
            DockerResponseDto response = getContainerInspect(containerId);
            if (response.isSuccess()) {
                ContainerInspectDto containerInspectDto = (ContainerInspectDto) response.getData();
                for (ContainerMountsDto mount : containerInspectDto.getMounts()) {
                    String mountName = mount.getName();
                    if (mountName != null && mountName.equalsIgnoreCase(volumeId)) {
                        containerNameList.add(containerInspectDto.getName().replace("/",""));
                        break; // 이미 찾았으므로 루프 종료
                    }
                }
            }
        }
        return containerNameList;
    }

    public List<String> getContainerIdList() {
        CommandExecuteResponse result = this.dockerCommandUtil.execute("docker ps -a | awk '{ print $1 }'");
        if (result.isSuccess()) {
            String[] array = result.getData().replace(")", "").replace("CONTAINER", "").trim().split("\\n");
            return (List) Arrays.stream(array).collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
