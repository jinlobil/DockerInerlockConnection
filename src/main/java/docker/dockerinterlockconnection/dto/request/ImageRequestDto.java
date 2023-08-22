package docker.dockerinterlockconnection.dto.request;

import lombok.Data;

@Data
public class ImageRequestDto {
    private String imageName;
    private String imageVersion;
}
