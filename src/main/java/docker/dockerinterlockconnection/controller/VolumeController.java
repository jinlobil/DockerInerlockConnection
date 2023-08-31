package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.dto.request.VolumeRequestDto;
import docker.dockerinterlockconnection.dto.response.DockerWebSocketResponse;
import docker.dockerinterlockconnection.service.DockerService;
import docker.dockerinterlockconnection.service.VolumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("/docker/volume")
@RequiredArgsConstructor
public class VolumeController {
    private final VolumeService volumeService;
    @GetMapping("")
    public DockerResponseDto getVolumeList(){
        return volumeService.getVolumeList();
    }
    @PostMapping("")
    public DockerResponseDto createVolume(@RequestBody VolumeRequestDto volumeRequestDto){
        CompletableFuture.supplyAsync(() -> {
            this.volumeService.createVolume(volumeRequestDto.getVolumeId());
            return null;
        });
        return new DockerResponseDto(true, "Volume created request completed", null);
    }
    @DeleteMapping("/{volumeId}")
    public DockerResponseDto deleteVolume(@PathVariable(value = "volumeId") String volumeId){
        CompletableFuture.supplyAsync(() -> {
            this.volumeService.deleteVolume(volumeId);
            return null;
        });
        return new DockerResponseDto(true, "Volume deleted request completed", null);
    }

}
