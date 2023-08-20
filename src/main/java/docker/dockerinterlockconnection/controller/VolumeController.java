package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.DockerResponseDto;
import docker.dockerinterlockconnection.dto.VolumeRequestDto;
import docker.dockerinterlockconnection.service.VolumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{volumeId}")
    public DockerResponseDto getVolumeInspect(@PathVariable(value = "volumeId") String volumeId){
        return volumeService.getVolumeInspect(volumeId);
    }
    @GetMapping("")
    public DockerResponseDto createVolume(VolumeRequestDto volumeRequestDto){
        return volumeService.createVolume(volumeRequestDto.getVolumeId());
    }
    @DeleteMapping("/{volumeId}")
    public DockerResponseDto deleteVolume(@PathVariable(value = "volumeId") String volumeId){
        return volumeService.deleteVolume(volumeId);
    }

}
