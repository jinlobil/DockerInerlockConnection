package docker.dockerinterlockconnection.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VolumeResponseDto {
    private Integer containerCount;
    private String mountPoint;
    private String volumeName;
    private VolumeInspectResponseDto inspect;
}
