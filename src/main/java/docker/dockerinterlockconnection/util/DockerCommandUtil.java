package docker.dockerinterlockconnection.util;

import docker.dockerinterlockconnection.dto.CommandExecuteResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DockerCommandUtil {
    public CommandExecuteResponse execute(String cmd) {
        log.info("cmd {}", cmd);
        if (cmd == null) {
            return new CommandExecuteResponse(false, "-1", "Command is Null");
        }

        Process process = CommandExecutor.executeCommand(cmd);
        return new CommandUtil().readProcessPrintData(process);
    }
}
