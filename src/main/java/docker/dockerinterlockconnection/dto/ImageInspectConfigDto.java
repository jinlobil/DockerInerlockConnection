package docker.dockerinterlockconnection.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageInspectConfigDto {
    @JsonProperty("Env")
    private List<String> env;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("Labels")
    private Map<String, Object> labels;

}
