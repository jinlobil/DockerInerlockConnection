package docker.dockerinterlockconnection.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerInspectHostConfigDto {
    @JsonProperty("PortBindings")
    private ContainerPortBindDto portBind;
}
