package docker.dockerinterlockconnection.dto.response;

import lombok.Data;

@Data
public class ContainerMountResponseDto {
    private String type;
    private String name;
    private String source;
    private String destination;
    private String driver;
    private String mode;
    private Boolean rw;
}
