package docker.dockerinterlockconnection.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ImageInspectResponseDto {
    private String size;
    private String createdAt;
    private String imageId;

    private String Architecture;
    private String os;
    private List<String> env;
}
