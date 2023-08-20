package docker.dockerinterlockconnection.service;

import docker.dockerinterlockconnection.dto.ContainerResponseDto;
import docker.dockerinterlockconnection.dto.VolumeResponseDto;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerCacheDataService {
    private List<ContainerResponseDto> containerCacheData;
    private List<VolumeResponseDto> volumeCacheData;
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
    public Object getVolumeCacheData(){
        return this.volumeCacheData;
    }
    public void setVolumeCacheData(List<VolumeResponseDto> cacheData) {
        this.volumeCacheData = cacheData;
    }
}
