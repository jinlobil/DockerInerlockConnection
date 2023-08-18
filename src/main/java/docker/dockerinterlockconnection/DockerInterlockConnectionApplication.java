package docker.dockerinterlockconnection;

import docker.dockerinterlockconnection.controller.DockerController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DockerInterlockConnectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerInterlockConnectionApplication.class, args);
        DockerController dockerController = new DockerController();
        boolean auth = dockerController.auth();
        if (auth){
            // 여기서 수집기 돌려야 함
        }else {
            //docker 인증 실패
        }
    }

}
