package docker.dockerinterlockconnection.dto;

import lombok.Data;

@Data
public class ImageRequestDto {
    private String imageName;
    private String imageVersion;
}
