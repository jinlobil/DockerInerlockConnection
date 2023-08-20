package docker.dockerinterlockconnection.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ContainerRequestDto {
    private String containerName;
    private String imageId;
    private List<MountDto> mountList;
    private Map<String, String> env;
    private List<String> publishList;
}
