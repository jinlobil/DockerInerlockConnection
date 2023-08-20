package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeResponseDto {
    @JsonProperty("Driver")
    private String driver;
    @JsonProperty("Labels")
    private String labels;
    @JsonProperty("Links")
    private Integer containerCount;
    @JsonProperty("Mountpoint")
    private String mountPoint;
    @JsonProperty("Name")
    private String volumeName;
    @JsonProperty("Scope")
    private String scope;
    @JsonProperty("Size")
    private String size;
}
