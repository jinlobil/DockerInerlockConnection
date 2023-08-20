package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageInspectDto {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("RepoTags")
    private List<String> RepoTags;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("Os")
    private String os;
    @JsonProperty("Architecture")
    private String architecture;
    @JsonProperty("Config")
    private ImageInspectConfigDto config;
}
