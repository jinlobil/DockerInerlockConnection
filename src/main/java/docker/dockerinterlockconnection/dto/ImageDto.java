package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto {
    @JsonProperty("Containers")
    private String usedContainerCount;
    @JsonProperty("CreatedAt")
    private String createdAt;
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Repository")
    private String imageName;
    @JsonProperty("Tag")
    private String imageVersion;
    @JsonProperty("Size")
    private String size;
    @JsonProperty("VirtualSize")
    private String virtualSize;
}
