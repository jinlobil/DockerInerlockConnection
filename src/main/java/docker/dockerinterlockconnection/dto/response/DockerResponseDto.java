package docker.dockerinterlockconnection.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DockerResponseDto {
    private boolean isSuccess;
    private String message;
    private Object data;
}
