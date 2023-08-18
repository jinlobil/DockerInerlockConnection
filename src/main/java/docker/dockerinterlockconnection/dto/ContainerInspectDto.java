package docker.dockerinterlockconnection.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerInspectDto {
    @JsonProperty("Id")
    private String containerId;
    @JsonProperty("State")
    private ContainerStateDto state;
    @JsonProperty("HostConfig")
    private ContainerInspectHostConfigDto hostConfig;
    @JsonProperty("Config")
    private ImageInspectConfigDto config;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Image")
    private String imageId;
    @JsonProperty("Mounts")
    private List<ContainerMountsDto> mounts;
    @JsonProperty("Networks")
    private ContainerNetworkDto networks;
}
