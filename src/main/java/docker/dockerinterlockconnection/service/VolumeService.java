package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.*;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VolumeService {
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final DockerService dockerService;
    private final ContainerService containerService;
    private final DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DockerResponseDto getVolumeList(){
        Object containerCacheData= this.dockerCacheDataService.getContainerCacheData();
        if (containerCacheData == null){
            return new DockerResponseDto(false,"Volume inquiry failed",null);
        }
        return new DockerResponseDto(true,"Volume inquiry completed",containerCacheData);
    }

    public DockerResponseDto getVolumeInspect(String volumeId){
        if (!dockerService.idValidationCheck(volumeId)) {
            return new DockerResponseDto(false, "volumeId is invalid", null);
        }
        if (volumeId == null){
            return new DockerResponseDto(false, "volumeId is null", null);
        }
        CommandExecuteResponse response = dockerCommandUtil.execute("docker volume inspect " + volumeId + " --format json");

        if (response.isSuccess()){
            List<VolumeInspectDto> inspectDtoList = null;
            try {
                inspectDtoList = this.objectMapper.readValue(response.getData(), new TypeReference<List<VolumeInspectDto>>() {
                });
            } catch (JsonProcessingException e) {
                log.error("VolumeService_getVolumeInspect Json Parsing Error {}", e);
            }
            if (inspectDtoList.size() == 0) {
                return new DockerResponseDto(false, "Volume inquiry failed", null);
            }
            inspectDtoList.get(0).setUseContainerNames(getVolumeUseContainerName(inspectDtoList.get(0).getVolumeName()));
            return new DockerResponseDto(true,"Volume inquiry completed", inspectDtoList.get(0));
        }
        return new DockerResponseDto(false,"Volume inquiry failed",null);
    }

    public DockerResponseDto createVolume(String volumeId) {
        StringBuilder cmd = new StringBuilder();
        if (volumeId == null) {
            return new DockerResponseDto(false, "VolumeName is null", null);
        }
        cmd.append("docker volume create ").append(volumeId);
        CommandExecuteResponse response = dockerCommandUtil.execute(cmd.toString());
        if (response.isSuccess()){
            return new DockerResponseDto(true,"Volume created completed",response.getData());
        }else {
            return new DockerResponseDto(false,"Volume created failed",null);
        }
    }

    public DockerResponseDto deleteVolume(String volumeId) {
        StringBuilder cmd = new StringBuilder();
        if (volumeId == null) {
            return new DockerResponseDto(false, "VolumeName is null", null);
        }
        cmd.append("docker volume rm ").append(volumeId);
        CommandExecuteResponse response = dockerCommandUtil.execute(cmd.toString());
        if (response.isSuccess()){
            return new DockerResponseDto(true,"Volume deleted completed",response.getData());
        }else {
            return new DockerResponseDto(false,"Volume deleted failed",null);
        }
    }

    public List<String> getVolumeUseContainerName(String volumeId){
        List<String> containerNameList = new ArrayList<>();
        for (String containerId : containerService.getContainerIdList()) {
            DockerResponseDto response = containerService.getContainerInspect(containerId);
            if (response.isSuccess()){
                ContainerInspectDto containerInspectDto= (ContainerInspectDto) response.getData();
                for (ContainerMountsDto mount : containerInspectDto.getMounts()) {
                    String mountName = mount.getName();
                    if (mountName != null && mountName.equalsIgnoreCase(volumeId)) {
                        containerNameList.add(containerInspectDto.getName());
                        break; // 이미 찾았으므로 루프 종료
                    }
                }
            }
        }
        return containerNameList;
    }
}
