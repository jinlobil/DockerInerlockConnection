package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContainerNetworkIpamConfigDto {

    @JsonProperty("IPv4Address")
    private String ipv4Address;

}
