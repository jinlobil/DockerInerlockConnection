package docker.dockerinterlockconnection.service;

import docker.dockerinterlockconnection.dto.*;
import docker.dockerinterlockconnection.dto.response.ContainerResponseDto;
import docker.dockerinterlockconnection.dto.response.ImageResponseDto;
import docker.dockerinterlockconnection.dto.response.SystemInfoResponseDto;
import docker.dockerinterlockconnection.dto.response.VolumeResponseDto;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerCacheDataService {
    private List<ContainerResponseDto> containerCacheData;
    private List<VolumeResponseDto> volumeCacheData;
    private List<ImageResponseDto> imageCacheData;
    private SystemInfoResponseDto systemInfoCacheData;
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
    public Object getImageCacheData(){
        return this.imageCacheData;
    }
    public void setImageCacheData(List<ImageResponseDto> cacheData) {
        this.imageCacheData = cacheData;
    }

    public Object getSystemInfoCacheData(){
        return this.systemInfoCacheData;
    }
    public void setSystemInfoCacheData(SystemInfoResponseDto cacheData) {
        this.systemInfoCacheData = cacheData;
    }
}
