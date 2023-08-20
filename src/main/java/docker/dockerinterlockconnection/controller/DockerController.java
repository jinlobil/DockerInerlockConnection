package docker.dockerinterlockconnection.controller;

import docker.dockerinterlockconnection.dto.CommandExecuteResponse;
import docker.dockerinterlockconnection.util.DockerCommandUtil;
import lombok.Data;

@Data
public class DockerController {
    private DockerCommandUtil dockerCommandUtil = new DockerCommandUtil();

    public boolean auth(){
        CommandExecuteResponse dockerVersion = dockerCommandUtil.execute("docker version");
//        if (!dockerVersion.isSuccess()) {
//            return false;
//        }else {
//            return true;
//        }
        return dockerVersion.isSuccess();
    }
}
