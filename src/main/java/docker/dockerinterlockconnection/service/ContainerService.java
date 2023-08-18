package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.CommandExecuteResponse;
import docker.dockerinterlockconnection.dto.ContainerInspectDto;
import docker.dockerinterlockconnection.dto.ContainerResponseDto;
import docker.dockerinterlockconnection.dto.DockerResponseDto;
import docker.dockerinterlockconnection.util.CommandExecutor;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContainerService {
    private final DockerService dockerService;
    private final DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();
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
                log.error("ContainerInterlock_getContainerInspect Json Parsing Error {}", e);
            }
            if (inspectDtoList.size() == 0) {
                return new DockerResponseDto(false, "Container inquiry failed", null);
            }
            return new DockerResponseDto(true,"Container inquiry completed", inspectDtoList.get(0));
        }
        return new DockerResponseDto(false,"Container inquiry failed",null);
    }
}
