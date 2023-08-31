package docker.dockerinterlockconnection.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageResponseDto {
    private Integer containerCount;
    private String imageName;
    private String imageVersion;
    private ImageInspectResponseDto inspect;

}
