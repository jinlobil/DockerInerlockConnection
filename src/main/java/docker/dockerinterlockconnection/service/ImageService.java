package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.*;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final DockerService dockerService;
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DockerResponseDto getLocalImageList(){
        Object ImageCacheData= this.dockerCacheDataService.getContainerCacheData();
        if (ImageCacheData == null){
            return new DockerResponseDto(false,"Image inquiry failed",null);
        }
        return new DockerResponseDto(true,"Image inquiry completed",ImageCacheData);
    }

    public DockerResponseDto getLocalImageInspect(String imageId){
        if (!dockerService.idValidationCheck(imageId)){
            return new DockerResponseDto(false, "imageId is invalid", null);
        }
        String cmd = "docker image inspect " + imageId + " --format json";
        CommandExecuteResponse response = dockerCommandUtil.execute(cmd);
        if (response.isSuccess()) {
            List<ImageInspectDto>  inspectDtoList = null;
            try {
                inspectDtoList = objectMapper.readValue(response.getData(), new TypeReference<List<ImageInspectDto>>() {

                });
            } catch (JsonProcessingException e) {
                log.error("ImageService_getLocalImageInspect Json Parsing Error {}", e);
            }
            if (inspectDtoList.size() == 0) {
                return new DockerResponseDto(false, "Image inquiry failed", null);
            }
            return new DockerResponseDto(true,"Image inquiry completed", inspectDtoList.get(0));
        }
        return new DockerResponseDto(false, "Image inquiry failed", null);
    }

    

}
