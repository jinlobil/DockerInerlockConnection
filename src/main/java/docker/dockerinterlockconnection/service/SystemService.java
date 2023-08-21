package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.CommandExecuteResponse;
import docker.dockerinterlockconnection.dto.DockerResponseDto;
import docker.dockerinterlockconnection.dto.SystemResponseDto;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemService {
    DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final ObjectMapper objectMapper=  new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public DockerResponseDto getSystemInfo(){
        CommandExecuteResponse response = dockerCommandUtil.execute("docker system df -v --format json");
        if (response.isSuccess()){
            try {
                return new DockerResponseDto(true,"System inquiry completed", this.objectMapper.readValue(response.getData(), SystemResponseDto.class));
            } catch (JsonProcessingException e) {
                log.error("SystemService_getSystemInfo Json Parsing Error {}", e);
            }
        }
        return new DockerResponseDto(false,"System inquiry failed",null);
    }
}
