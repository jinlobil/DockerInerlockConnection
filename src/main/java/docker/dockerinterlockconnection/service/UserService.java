package docker.dockerinterlockconnection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.BasicAuthDto;
import docker.dockerinterlockconnection.dto.response.CommandExecuteResponse;
import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserCacheDataService userCacheDataService = UserCacheDataService.getUserCacheDataService();
    private final DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private final DockerService dockerService;
    public DockerResponseDto dockerHubLogin(String authorizationData) {
        BasicAuthDto basicAuth = this.dockerService.basicAuthDecode(authorizationData);
        if (basicAuth.isSuccess()) {
            CommandExecuteResponse executeResponse = this.dockerCommandUtil.execute("echo " + basicAuth.getPassword() + " | docker login -u " + basicAuth.getUsername() + " --password-stdin");
            if (executeResponse.isSuccess()) {
                this.userCacheDataService.setAuthorizationData(authorizationData);
                return new DockerResponseDto(true, "DockerHub login successful",executeResponse.getData());
            }else {
                return new DockerResponseDto(false, "DockerHub login failed", null);
            }
        }
        return new DockerResponseDto(false, "DockerHub login failed", null);
    }

    public  DockerResponseDto dockerHubLogout() {
        if (this.userCacheDataService.getAuthorizationData() == null) {
            CommandExecuteResponse executeResponse = this.dockerCommandUtil.execute("docker logout");
            if (executeResponse.isSuccess()) {
                this.userCacheDataService.deleteAuthorizationData();
                return new DockerResponseDto(true, "DockerHub logout successful",executeResponse.getData());
            }else {
                return new DockerResponseDto(false, "DockerHub logout failed", null);
            }
        }
        return new DockerResponseDto(false, "DockerHub logout failed", null);
    }
}
