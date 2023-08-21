package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SystemResponseDto {
    @JsonProperty("Images")
    private List<ImageResponseDto> imageDtoList;
    @JsonProperty("Containers")
    private List<ContainerResponseDto> containerDtoList;
    @JsonProperty("Volumes")
    private List<VolumeResponseDto> volumeDtoList;
}
