package docker.dockerinterlockconnection.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ContainerResponseDto {
    private Integer volumeCount;
    private String containerName;
    private String state;
    private ContainerInspectResponseDto inspect;
}
