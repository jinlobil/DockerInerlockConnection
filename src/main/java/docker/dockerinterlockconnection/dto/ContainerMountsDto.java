package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerMountsDto {
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Source")
    private String source;
    @JsonProperty("Destination")
    private String destination;
    @JsonProperty("Driver")
    private String driver;
    @JsonProperty("Mode")
    private String mode;
    @JsonProperty("RW")
    private Boolean rw;
}
