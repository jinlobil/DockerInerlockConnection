package docker.dockerinterlockconnection.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageResponseDto {
    private String usedContainerCount;
    private String createdAt;
    private String imageId;
    private String imageName;
    private String imageVersion;
    private String size;
}
