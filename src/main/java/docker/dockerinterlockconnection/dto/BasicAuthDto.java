package docker.dockerinterlockconnection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicAuthDto {
    private boolean isSuccess;
    private String username;
    private String password;
}
