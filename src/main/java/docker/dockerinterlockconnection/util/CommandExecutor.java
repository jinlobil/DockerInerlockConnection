package docker.dockerinterlockconnection.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandExecutor {
    @SneakyThrows
    public static Process executeCommand(String cmd) {
        log.trace("cmd: {}", cmd);
        String[] command;

        String osName = System.getProperty("os.name");
        boolean isWindows = osName.toLowerCase().startsWith("windows");

        log.trace("os.name: {}, isWindows: {}", osName, isWindows);
        if (isWindows) {
            command = new String[]{"cmd", "/c", cmd};
        } else {
            command = new String[]{"sh", "-c", cmd};
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();
        return process;
    }
}
