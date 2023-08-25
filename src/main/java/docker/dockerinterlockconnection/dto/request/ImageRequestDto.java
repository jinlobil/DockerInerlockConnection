package docker.dockerinterlockconnection.dto.request;

import lombok.Data;

@Data
public class ImageRequestDto {
    private String imageId;
    private String imageName;
    private String imageVersion;
    private String setImageName;
    private String setImageVersion;
}
