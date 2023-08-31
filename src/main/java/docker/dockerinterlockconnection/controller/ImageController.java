package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.dto.request.ImageRequestDto;
import docker.dockerinterlockconnection.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

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

    @PostMapping("/local")
    public DockerResponseDto localImagePull(@RequestBody ImageRequestDto imageRequestDto){
        CompletableFuture.supplyAsync(() -> {
            this.imageService.localImagePull(imageRequestDto);
            return null;
        });
        return new DockerResponseDto(true, "Image pull request completed", null);
    }
    @DeleteMapping("/local/{imageId}")
    public DockerResponseDto deleteImage(@PathVariable(value = "imageId") String imageId){
        CompletableFuture.supplyAsync(() -> {
            this.imageService.deleteImage(imageId);
            return null;
        });
        return new DockerResponseDto(true, "Image deleted request completed", null);
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
