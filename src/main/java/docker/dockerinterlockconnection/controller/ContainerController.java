package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.request.ContainerRequestDto;
import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.service.ContainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("/docker/container")
@RequiredArgsConstructor
public class ContainerController {
    private final ContainerService containerService;

    @GetMapping("")
    public DockerResponseDto getContainerList(){
       return containerService.getContainerList();
    }

    @PostMapping ("")
    public DockerResponseDto createContainer(@RequestBody ContainerRequestDto containerData){
        CompletableFuture.supplyAsync(() -> {
            this.containerService.createContainer(containerData);
            return null;
        });
        return new DockerResponseDto(true, "Container created request completed", null);
    }
    @PutMapping ("/status")
    public DockerResponseDto statusControl(@RequestBody ContainerRequestDto containerData){
        CompletableFuture.supplyAsync(() -> {
            this.containerService.statusControl(containerData);
            return null;
        });
        return new DockerResponseDto(true, "Container status request completed", null);
    }
    @DeleteMapping ("/{containerId}")
    public DockerResponseDto deleteContainer(@PathVariable(value = "containerId") String containerId){
        CompletableFuture.supplyAsync(() -> {
            this.containerService.deleteContainer(containerId);
            return null;
        });
        return new DockerResponseDto(true, "Container deleted request completed", null);
    }
}
