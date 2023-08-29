package docker.dockerinterlockconnection.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ContainerInspectResponseDto {
    private String createdAt;
    private String containerId;
    private String imageId;
    private String size;
    private List<ContainerMountResponseDto> mounts;
    private List<String> ports;
}
