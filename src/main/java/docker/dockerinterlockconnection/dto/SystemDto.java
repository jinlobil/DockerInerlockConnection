package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SystemDto {
    @JsonProperty("Images")
    private List<ImageDto> imageDtoList;
    @JsonProperty("Containers")
    private List<ContainerDto> containerDtoList;
    @JsonProperty("Volumes")
    private List<VolumeDto> volumeDtoList;
}
