package docker.dockerinterlockconnection.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class VolumeInspectResponseDto {
    private String driver;
    private String scope;
    private String size;
    private String createdAt;
    private List<String> labels;
    private List<String> containerNames;
}
