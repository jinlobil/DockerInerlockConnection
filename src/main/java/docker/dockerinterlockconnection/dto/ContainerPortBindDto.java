package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerPortBindDto {
    private Map<String, Object> portsMap = new HashMap<>();
    @JsonAnySetter
    public void addPortDto(String name, Object value) {
        portsMap.put(name, value);
    }
}
