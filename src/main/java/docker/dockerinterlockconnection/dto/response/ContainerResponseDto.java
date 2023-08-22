package docker.dockerinterlockconnection.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ContainerResponseDto {
    private String createdAt;
    private String containerId;
    private String imageId;
//    private List<String> labels;
    private Integer volumeCount;
//    private List<String> mounts;
    private String containerName;
//    private List<String> ports;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("State")
    private String state;
}
