package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.collector.DockerCollectorJob;
import docker.dockerinterlockconnection.dto.CommandExecuteResponse;
import docker.dockerinterlockconnection.service.DockerService;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
public class DockerController {
    private DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    public boolean auth(){
        CommandExecuteResponse dockerVersion = dockerCommandUtil.execute("docker version");

        return dockerVersion.isSuccess();
    }
}
