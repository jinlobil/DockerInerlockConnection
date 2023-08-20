package docker.dockerinterlockconnection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.CommandExecuteResponse;
import docker.dockerinterlockconnection.dto.ContainerRequestDto;
import docker.dockerinterlockconnection.dto.DockerResponseDto;
import docker.dockerinterlockconnection.service.ContainerService;
import docker.dockerinterlockconnection.service.DockerCacheDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
        Object containerCacheData= this.dockerCacheDataService.getContainerCacheData();
        if (containerCacheData == null){
            return new DockerResponseDto(false,"Container inquiry failed",null);
        }
        return new DockerResponseDto(true,"Container inquiry completed",containerCacheData);
    }
    @GetMapping("/container/{containerId}")
    public DockerResponseDto getContainerInspect(@PathVariable(value = "containerId") String containerId){
        return containerService.getContainerInspect(containerId);
    }
    @PostMapping ("/container")
    public DockerResponseDto createContainer(ContainerRequestDto containerData){
        return containerService.createContainer(containerData);
    }
    @DeleteMapping ("/container/{containerId}")
    public DockerResponseDto deleteContainer(@PathVariable(value = "containerId") String containerId){
        return containerService.deleteContainer(containerId);
    }
}
