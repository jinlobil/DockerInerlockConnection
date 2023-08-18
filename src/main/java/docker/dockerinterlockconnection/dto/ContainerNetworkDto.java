package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerNetworkDto {

    @JsonProperty("IPAMConfig")
    private ContainerNetworkIpamConfigDto ipamConfig;
    @JsonProperty("NetworkID")
    private String networkID;
    @JsonProperty("EndpointID")
    private String endpointID;
    @JsonProperty("Gateway")
    private String gateway;
    @JsonProperty("IPAddress")
    private String iPAddress;
    @JsonProperty("IPPrefixLen")
    private Integer iPPrefixLen;
    @JsonProperty("MacAddress")
    private String macAddress;

}
