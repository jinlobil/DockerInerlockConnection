package docker.dockerinterlockconnection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandExecuteResponse {
    private boolean success;
    private String errorCode;
    private String data;
}
