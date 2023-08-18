package docker.dockerinterlockconnection.service;

import org.springframework.stereotype.Service;

@Service
public class DockerService {
    public boolean idValidationCheck(String id) {
        return id.matches("^[A-Za-z0-9:]+$");
    }
}
