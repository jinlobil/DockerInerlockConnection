package docker.dockerinterlockconnection.dto;

import lombok.Data;

@Data
public class MountDto {
    private Integer mountType; // 0: volume, 1: bind, 2: tmpfs
    private String mountSource;
    private String mountTarget;
    private String mountDestination;
    private Boolean mountReadOnly = false;
}

