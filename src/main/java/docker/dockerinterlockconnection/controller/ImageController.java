package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.DockerResponseDto;
import docker.dockerinterlockconnection.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/docker/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @GetMapping("")
    public DockerResponseDto getImageList(){
        return imageService.getLocalImageList();
    }
    @GetMapping("/{imageId}")
    public DockerResponseDto getContainerInspect(@PathVariable(value = "imageId") String imageId){
        return imageService.getLocalImageInspect(imageId);
    }
}
