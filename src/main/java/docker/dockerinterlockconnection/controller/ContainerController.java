package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.request.ContainerRequestDto;
import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.service.ContainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/docker/container")
@RequiredArgsConstructor
public class ContainerController {
    private final ContainerService containerService;

    @GetMapping("")
    public DockerResponseDto getContainerList(){
        log.info("잘 들어 오나요?");
       return containerService.getContainerList();
    }
    @GetMapping("/{containerId}")
    public DockerResponseDto getContainerInspect(@PathVariable(value = "containerId") String containerId){
        return containerService.getContainerInspect(containerId);
    }
    @PostMapping ("")
    public DockerResponseDto createContainer(ContainerRequestDto containerData){
        return containerService.createContainer(containerData);
    }
    @DeleteMapping ("/{containerId}")
    public DockerResponseDto deleteContainer(@PathVariable(value = "containerId") String containerId){
        return containerService.deleteContainer(containerId);
    }
}
