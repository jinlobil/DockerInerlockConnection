package docker.dockerinterlockconnection;

import docker.dockerinterlockconnection.controller.DockerController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class DockerInterlockConnectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerInterlockConnectionApplication.class, args);
        DockerController dockerController = new DockerController();
        boolean auth = dockerController.auth();
        if (auth) {
            log.info("Authentication completed. Starting... docker_version_auth = {}", true);

        } else {
            //docker 인증 실패
            log.error("Authentication failed. Exiting... docker_version_auth = {}", false);

            System.exit(1); // 프로세스 종료
        }
    }

}
