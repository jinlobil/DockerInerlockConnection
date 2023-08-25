package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.response.DockerResponseDto;
import docker.dockerinterlockconnection.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docker/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/dockerhub/login")
    public DockerResponseDto dockerHubLogin(@RequestHeader("Authorization") String authorizationHeader) {
        return this.userService.dockerHubLogin(authorizationHeader);
    }

    @DeleteMapping("/dockerhub/logout")
    public DockerResponseDto dockerHubLogout() {
        return this.userService.dockerHubLogout();
    }
}
