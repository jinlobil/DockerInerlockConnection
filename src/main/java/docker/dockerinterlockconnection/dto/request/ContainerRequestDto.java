package docker.dockerinterlockconnection.dto.request;

import docker.dockerinterlockconnection.dto.MountDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ContainerRequestDto {
    private String containerName;
    private String containerId;
    private String imageId;
    private Integer status;
    private List<MountDto> mountList;
    private Map<String, String> env;
    private List<String> publishList;
}
