package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.collector.DockerCollectorJob;
import docker.dockerinterlockconnection.dto.*;
import docker.dockerinterlockconnection.dto.request.ImageRequestDto;
import docker.dockerinterlockconnection.dto.response.CommandExecuteResponse;
import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.dto.response.DockerWebSocketResponse;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final DockerService dockerService;
    private final DockerCollectorJob dockerCollectorJob;
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final UserCacheDataService userCacheDataService = UserCacheDataService.getUserCacheDataService();
    private final DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DockerResponseDto getLocalImageList(){
        Object ImageCacheData= this.dockerCacheDataService.getImageCacheData();
        if (ImageCacheData == null){
            return new DockerResponseDto(false,"Image inquiry failed",null);
        }
        return new DockerResponseDto(true,"Image inquiry completed",ImageCacheData);
    }


    public DockerResponseDto localImagePull(ImageRequestDto imageData) {
        if (imageData == null || imageData.getImageName() == null) {
            this.dockerService.notifyAlarm(false, "ImageName is null", DockerWebSocketResponse.DataType.IMAGE);
            return new DockerResponseDto(false, "ImageName is null", null);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("docker image pull ").append(imageData.getImageName());
        if (imageData.getImageVersion() != null) {
            sb.append(":").append(imageData.getImageVersion());
        }

        CommandExecuteResponse response = this.dockerCommandUtil.execute(sb.toString());
        DockerResponseDto dockerResponseDto = null;
        if (response.isSuccess()){
            dockerResponseDto = new DockerResponseDto(true,"Image pull completed",response.getData());
        }else {
            dockerResponseDto = new DockerResponseDto(false, "Image pull failed", null);
        }
        this.dockerService.notifyTotal(dockerResponseDto, DockerWebSocketResponse.DataType.IMAGE);
        return dockerResponseDto;
    }

    public DockerResponseDto deleteImage(String imageId) {
        if (imageId == null) {
            this.dockerService.notifyAlarm(false, "ImageId is null", DockerWebSocketResponse.DataType.IMAGE);
            return new DockerResponseDto(false, "ImageId is null", null);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("docker image rm ").append(imageId);

        CommandExecuteResponse response = this.dockerCommandUtil.execute(sb.toString());
        DockerResponseDto dockerResponseDto = null;
        if (response.isSuccess()){
            dockerResponseDto = new DockerResponseDto(true,"Image deleted completed",response.getData());
        }else {
            dockerResponseDto = new DockerResponseDto(false, "Image deleted failed", null);
        }
        this.dockerService.notifyTotal(dockerResponseDto, DockerWebSocketResponse.DataType.IMAGE);
        return dockerResponseDto;
    }

    public DockerResponseDto dockerHubImageSearch(String searchData) {
        if (searchData == null) {
            return new DockerResponseDto(false, "Search String is null", null);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("docker search ").append(searchData).append(" --no-trunc");
        CommandExecuteResponse response = this.dockerCommandUtil.execute(sb.toString());
        if (response.isSuccess() && response.getData() != null) {
            List<ImageSearchDto> imageSearchList = new ArrayList<>();
            String rawData = response.getData();
            Pattern pattern = Pattern.compile(".+");
            Matcher matcher = pattern.matcher(rawData);
            while (matcher.find()){
                String[] rawSplitArrayData = matcher.group().split("\\s{2,}");
                ImageSearchDto imageSearchDto = new ImageSearchDto();
                if (rawSplitArrayData[0] != null) {
                    imageSearchDto.setName(rawSplitArrayData[0]);
                }
                if (rawSplitArrayData[1] != null) {
                    imageSearchDto.setDescription(rawSplitArrayData[1]);
                }
                if (rawSplitArrayData[2] != null) {
                    imageSearchDto.setStars(Integer.valueOf(rawSplitArrayData[2]));
                }
                imageSearchList.add(imageSearchDto);
            }
            return new DockerResponseDto(true, "Image search completed", imageSearchList);
        }
        return new DockerResponseDto(false, "Image search failed", null);
    }

    public DockerResponseDto dockerHubImageUpload(ImageRequestDto imageData) {
        if (imageData.getImageName() == null) {
            this.dockerService.notifyAlarm(false, "ImageName is null", DockerWebSocketResponse.DataType.IMAGE);
            return new DockerResponseDto(false, "ImageName is null", null);
        }
        if (imageData.getImageVersion() == null) {
            this.dockerService.notifyAlarm(false, "ImageVersion is null", DockerWebSocketResponse.DataType.IMAGE);
            return new DockerResponseDto(false, "ImageVersion is null", null);
        }
        String authorizationData= this.userCacheDataService.getAuthorizationData();
        if (authorizationData == null) {

        }else {
            BasicAuthDto basicAuthInfo = this.dockerService.basicAuthDecode(authorizationData);
            String name = null;
            String version = null;
            StringBuilder sb = new StringBuilder();
            sb.append("docker tag ").append(imageData.getImageName()).append(":").append(imageData.getImageVersion()).append(" ").append(basicAuthInfo.getUsername()).append("/");
            if (imageData.getSetImageName() != null) {
                name = imageData.getSetImageName();
                sb.append(imageData.getSetImageName());
            }else {
                name = imageData.getImageName();
                sb.append(imageData.getImageName());
            }
            if (imageData.getSetImageVersion() != null) {
                version = imageData.getSetImageVersion();
                sb.append(":").append(imageData.getSetImageVersion());
            }else {
                version = imageData.getImageVersion();
                sb.append(":").append(imageData.getImageVersion());
            }
            CommandExecuteResponse uploadResponse = this.dockerCommandUtil.execute(sb.toString());
            if (uploadResponse.isSuccess()){
                this.dockerService.notifyAlarm(true, "Image tag completed", DockerWebSocketResponse.DataType.IMAGE);
                StringBuilder pushSb = new StringBuilder();
                pushSb.append("docker push ").append(basicAuthInfo.getUsername()).append("/").append(name).append(":").append(version);
                CommandExecuteResponse pushResponse = this.dockerCommandUtil.execute(pushSb.toString());
                if (pushResponse.isSuccess()) {
                    this.dockerService.notifyAlarm(true, "Image push completed", DockerWebSocketResponse.DataType.IMAGE);
                    return new DockerResponseDto(true, "Image push completed", uploadResponse.getData());
                }else {
                    this.dockerService.notifyAlarm(false, "Image push failed", DockerWebSocketResponse.DataType.IMAGE);
                    return new DockerResponseDto(false, "Image push failed", uploadResponse.getData());
                }
            }else {
                this.dockerService.notifyAlarm(false, "Image tag failed", DockerWebSocketResponse.DataType.IMAGE);
                return new DockerResponseDto(false, "Image tag failed", uploadResponse.getData());
            }
        }
        return null;
    }

}
