package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/docker/system")
@RequiredArgsConstructor
public class SystemController {
    private final SystemService systemService;
    @GetMapping("/info")
    public DockerResponseDto getSystemInfo(){
        return this.systemService.getSystemInfoData();
    }
}
