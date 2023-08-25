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
    @GetMapping("/local")
    public DockerResponseDto getImageList(){
        return this.imageService.getLocalImageList();
    }
    @GetMapping("/local/{imageId}")
    public DockerResponseDto getContainerInspect(@PathVariable(value = "imageId") String imageId){
        return this.imageService.getLocalImageInspect(imageId);
    }
    @PostMapping("/local")
    public DockerResponseDto localImagePull(@RequestBody ImageRequestDto imageRequestDto){
        return this.imageService.localImagePull(imageRequestDto);
    }
    @DeleteMapping("/local/{imageId}")
    public DockerResponseDto deleteImage(@PathVariable(value = "imageId") String imageId){
        return this.imageService.deleteImage(imageId);
    }

    @GetMapping("/hub/search/{search-word}")
    private DockerResponseDto dockerHubImageSearch(@PathVariable(value = "search-word") String searchWord){
        return this.imageService.dockerHubImageSearch(searchWord);
    }
    @PostMapping("/hub/upload")
    public DockerResponseDto dockerHubImageUpload(@RequestBody ImageRequestDto imageRequestDto){
        return this.imageService.dockerHubImageUpload(imageRequestDto);
    }
}
