package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInspectDto {
    @JsonProperty("CreatedAt")
    private String createdAt;
    @JsonProperty("Labels")
    private Map<String, Object> labels;
    @JsonProperty("Driver")
    private String driver;
    @JsonProperty("Mountpoint")
    private String mountPoint;
    @JsonProperty("Name")
    private String volumeName;
    @JsonProperty("Scope")
    private String scope;
    private List<String> useContainerNames;
}
