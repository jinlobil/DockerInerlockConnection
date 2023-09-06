package docker.dockerinterlockconnection.collector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import docker.dockerinterlockconnection.dto.response.ContainerResponseDto;
import docker.dockerinterlockconnection.dto.response.ImageResponseDto;
import docker.dockerinterlockconnection.dto.response.SystemInfoResponseDto;
import docker.dockerinterlockconnection.dto.response.VolumeResponseDto;
import docker.dockerinterlockconnection.entity.Container;
import docker.dockerinterlockconnection.entity.Image;
import docker.dockerinterlockconnection.entity.SystemInfo;
import docker.dockerinterlockconnection.entity.Volume;
import docker.dockerinterlockconnection.repository.ContainerRepository;
import docker.dockerinterlockconnection.repository.ImageRepository;
import docker.dockerinterlockconnection.repository.SystemRepository;
import docker.dockerinterlockconnection.repository.VolumeRepository;
import docker.dockerinterlockconnection.service.DockerCacheDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DockerDataProcessor {
    private final SystemRepository systemRepository;
    private final ContainerRepository containerRepository;
    private final ImageRepository imageRepository;
    private final VolumeRepository volumeRepository;
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Async("threadPoolTaskExecutor")
    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void dataRepository() {
        SystemInfoResponseDto systemInfoCacheData = (SystemInfoResponseDto) dockerCacheDataService.getSystemInfoCacheData();
        ContainerResponseDto containerCacheData = (ContainerResponseDto) dockerCacheDataService.getContainerCacheData();
        ImageResponseDto imageCacheData = (ImageResponseDto) dockerCacheDataService.getImageCacheData();
        VolumeResponseDto volumeCacheData = (VolumeResponseDto) dockerCacheDataService.getVolumeCacheData();

        // docker system
        if (systemInfoCacheData != null) {
            log.info("DockerDataProcessor_dataRepository START on thread: {}", Thread.currentThread().getName());
            SystemInfo systemInfo = new SystemInfo();

            systemInfo.setId(systemInfoCacheData.getDockerId() != null ? systemInfoCacheData.getDockerId() : null);
            systemInfo.setKernelVersion(systemInfoCacheData.getKernelVersion() != null ? systemInfoCacheData.getKernelVersion() : null);
            systemInfo.setOperatingSystem(systemInfoCacheData.getOperatingSystem() != null ? systemInfoCacheData.getOperatingSystem() : null);
            systemInfo.setOsVersion(systemInfoCacheData.getOsType() != null ? systemInfoCacheData.getOsType() : null);
            systemInfo.setArchitecture(systemInfoCacheData.getArchitecture() != null ? systemInfoCacheData.getArchitecture() : null);
            systemInfo.setDockerRootDir(systemInfoCacheData.getDockerRootDir() != null ? systemInfoCacheData.getDockerRootDir() : null);
            systemInfo.setServerVersion(systemInfoCacheData.getServerVersion() != null ? systemInfoCacheData.getServerVersion() : null);

            systemRepository.save(systemInfo);

            // container
            if (containerCacheData != null) {
                Container container = new Container();
                container.setId(containerCacheData.getContainerName() != null ? containerCacheData.getContainerName() : null);
                container.setSystemInfo(systemInfo);
                container.setVolumeCount(containerCacheData.getVolumeCount() != null ? containerCacheData.getVolumeCount() : null);
                container.setState(containerCacheData.getState() != null ? containerCacheData.getState() : null);
                container.setContainerName(container.getContainerName() != null ? containerCacheData.getContainerName() : null);
                container.setPorts(containerCacheData.getInspect().getPorts() != null ? containerCacheData.getInspect().getPorts().toString() : null);
                if (containerCacheData.getInspect().getMounts() != null) {
                    try {
                        String json = objectMapper.writeValueAsString(containerCacheData.getInspect().getMounts());
                        container.setMounts(json);
                    } catch (JsonProcessingException e) {
                        log.error("DockerDataRepository_dataRepository_container parsing error {}",e);
                    }
                }
                container.setSize(containerCacheData.getInspect().getSize() != null ? containerCacheData.getInspect().getSize() : null);
                container.setImageId(containerCacheData.getInspect().getImageId() != null ? containerCacheData.getInspect().getImageId() : null);
                container.setCreatedAt(containerCacheData.getInspect().getCreatedAt() != null ? containerCacheData.getInspect().getCreatedAt() : null);

                containerRepository.save(container);
            }
            // image
            if (imageCacheData != null) {
                Image image = new Image();

                image.setId(imageCacheData.getInspect().getImageId() != null ? imageCacheData.getInspect().getImageId() : null);
                image.setSystemInfo(systemInfo);
                image.setImageName(imageCacheData.getImageName() != null ? imageCacheData.getImageName() : null);
                image.setImageVersion(imageCacheData.getImageVersion() != null ? imageCacheData.getImageVersion() : null);
                image.setContainerCount(imageCacheData.getContainerCount() != null ? imageCacheData.getContainerCount() : null);
                image.setSize(imageCacheData.getInspect().getSize() != null ? imageCacheData.getInspect().getSize() : null);
                image.setEnv(imageCacheData.getInspect().getEnv() != null ? imageCacheData.getInspect().getEnv().toString() : null);
                image.setArchitecture(imageCacheData.getInspect().getArchitecture() != null ? imageCacheData.getInspect().getArchitecture() : null);
                image.setCreatedAt(imageCacheData.getInspect().getCreatedAt() != null ? imageCacheData.getInspect().getCreatedAt() : null);
                image.setOs(imageCacheData.getInspect().getOs() != null ? imageCacheData.getInspect().getOs() : null);

                imageRepository.save(image);
            }
            //volume
            if (volumeCacheData != null) {
                Volume volume = new Volume();

                volume.setId(volumeCacheData.getVolumeName() != null ? volumeCacheData.getVolumeName() : null);
                volume.setSystemInfo(systemInfo);
                volume.setVolumeName(volumeCacheData.getVolumeName() != null ? volumeCacheData.getVolumeName() : null);
                volume.setDriver(volumeCacheData.getInspect().getDriver() != null ? volumeCacheData.getInspect().getDriver() : null);
                volume.setMountPoint(volumeCacheData.getMountPoint() != null ? volumeCacheData.getMountPoint() : null);
                volume.setContainerCount(volumeCacheData.getContainerCount() != null ? volumeCacheData.getContainerCount() : null);
                volume.setScope(volumeCacheData.getInspect().getScope() != null ? volumeCacheData.getInspect().getScope() : null);
                volume.setSize(volumeCacheData.getInspect().getSize() != null ? volumeCacheData.getInspect().getSize() : null);
                volume.setCreatedAt(volumeCacheData.getInspect().getCreatedAt() !=null ? volumeCacheData.getInspect().getCreatedAt() : null);
                volume.setLabels(volumeCacheData.getInspect().getLabels() != null ? volumeCacheData.getInspect().getLabels().toString() : null);
                volume.setContainerNames(volumeCacheData.getInspect().getContainerNames() != null ? volumeCacheData.getInspect().getContainerNames().toString() : null);

                volumeRepository.save(volume);
            }
        } else {
            log.error("DockerDataProcessor_dataRepository Failed on thread: {}", Thread.currentThread().getName());
        }

        //
    }
}
