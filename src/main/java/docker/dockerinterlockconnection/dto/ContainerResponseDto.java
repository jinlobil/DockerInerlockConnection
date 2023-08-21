package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY) // 빈 문자열 또는 null인 경우 무시
public class ContainerResponseDto {
    @JsonProperty("CreatedAt")
    private String createdAt;
    @JsonProperty("ID")
    private String containerId;
    @JsonProperty("Image")
    private String imageId;
//    @JsonProperty("Labels")
//    private List<String> labels;
    @JsonProperty("LocalVolumes")
    private Integer volumeCount;
//    @JsonProperty("Mounts")
//    private List<String> mounts;
    @JsonProperty("Names")
    private String containerName;
//    @JsonProperty("Ports")
//    private List<String> ports;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("State")
    private String state;
}
