package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.DockerResponseDto;
import docker.dockerinterlockconnection.service.VolumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
