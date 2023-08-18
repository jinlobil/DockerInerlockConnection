package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerStateDto {
    @JsonProperty("Running")
    private boolean running;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("StartedAt")
    private String startedAt;
    @JsonProperty("FinishedAt")
    private String FinishedAt;
}
