package docker.dockerinterlockconnection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.CommandExecuteResponse;
import docker.dockerinterlockconnection.dto.DockerResponseDto;
import docker.dockerinterlockconnection.service.ContainerService;
import docker.dockerinterlockconnection.service.DockerCacheDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/docker")
public class ContainerController {
    private ContainerService containerService;
    private DockerCacheDataService dockerCacheDataService = null;

    public ContainerController(DockerCacheDataService dockerCacheDataService) {
        this.dockerCacheDataService = dockerCacheDataService;
    }

    @GetMapping("/container")
    public DockerResponseDto getContainerList(){
        return new DockerResponseDto(true,"Container inquiry completed",this.dockerCacheDataService.getContainerCacheData());
    }
    @GetMapping("/container/{containerId}")
    public DockerResponseDto getContainerInspect(@PathVariable(value = "containerId") String containerId){
        return containerService.getContainerInspect(containerId);
    }
}
