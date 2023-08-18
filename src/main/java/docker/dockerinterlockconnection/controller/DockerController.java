package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.CommandExecuteResponse;
import docker.dockerinterlockconnection.service.DockerCacheDataService;
import docker.dockerinterlockconnection.service.DockerService;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.Data;

@Data
public class DockerController {
    private DockerService dockerService;
    private DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();
    private ContainerController containerController = null;
    private DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();

    public boolean auth(){
        CommandExecuteResponse dockerVersion = dockerCommandUtil.execute("docker version");
        if (!dockerVersion.isSuccess()) {
            return false;
        }else {

            this.containerController = new ContainerController(dockerCacheDataService);

            return true;
        }
    }
}
