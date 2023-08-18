package docker.dockerinterlockconnection.service;

import docker.dockerinterlockconnection.dto.ContainerResponseDto;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerCacheDataService {
    private List<ContainerResponseDto> containerCacheData;
    @Getter
    private static final DockerCacheDataService dockerCacheDataService = new DockerCacheDataService();

    private DockerCacheDataService() {

    }

    public Object getContainerCacheData(){
        return this.containerCacheData;
    }
    public void setContainerCacheData(List<ContainerResponseDto> cacheData) {
        this.containerCacheData = cacheData;
    }
}
