package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY) // 빈 문자열 또는 null인 경우 무시
public class SystemDto {
    @JsonProperty("Images")
    private List<ImageDto> imageDtoList;
    @JsonProperty("Containers")
    private List<ContainerDto> containerDtoList;
    @JsonProperty("Volumes")
    private List<VolumeDto> volumeDtoList;
}
