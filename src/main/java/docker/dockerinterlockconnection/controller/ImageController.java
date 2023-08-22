package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.dto.request.ImageRequestDto;
import docker.dockerinterlockconnection.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("")
    public DockerResponseDto localImagePull(ImageRequestDto imageRequestDto){
        return imageService.localImagePull(imageRequestDto);
    }
    @DeleteMapping("/{imageId}")
    public DockerResponseDto deleteImage(@PathVariable(value = "imageId") String imageId){
        return imageService.deleteImage(imageId);
    }
}
