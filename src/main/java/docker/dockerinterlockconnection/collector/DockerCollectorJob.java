package docker.dockerinterlockconnection.collector;

import docker.dockerinterlockconnection.dto.*;
import docker.dockerinterlockconnection.dto.response.*;
import docker.dockerinterlockconnection.service.DockerCacheDataService;
import docker.dockerinterlockconnection.service.DockerService;
import docker.dockerinterlockconnection.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DockerCollectorJob {
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final DockerService dockerService;
    private final SystemService systemService;


    @Async("threadPoolTaskExecutor")
    //fixedDelay는 해당 작업이 끝난 시점부터 시간을 세고, fixedRate는 해당 작업의 시작 시점부터 시간을 세기 때문에 fixedDelay는 수집이 딜레이 될 가능성이 높음
    @Scheduled(initialDelay = 1000, fixedRate = 10000) // 10초마다 실행
    public void collect() {
        log.info("DockerCollectorJob_collect START on thread: {}", Thread.currentThread().getName());
        DockerResponseDto response = systemService.getSystemDataUsing();
        DockerResponseDto infoResponse = systemService.getSystemInfo();
        if (response.isSuccess()) {
            SystemDto systemData = (SystemDto) response.getData();

            // Container
            List<ContainerResponseDto> containerResponseList = new ArrayList<>();
            for (ContainerDto container : systemData.getContainerDtoList()) {
                ContainerResponseDto responseDto = new ContainerResponseDto();

                if (container.getCreatedAt() != null) {
                    responseDto.setCreatedAt(container.getCreatedAt());
                }
                if (container.getContainerId() != null) {
                    responseDto.setContainerId(container.getContainerId());
                }
                if (container.getImageId() != null) {
                    responseDto.setImageId(container.getImageId());
                }
                if (container.getVolumeCount() != null) {
                    responseDto.setVolumeCount(container.getVolumeCount());
                }
                if (container.getContainerName() != null) {
                    responseDto.setContainerName(container.getContainerName());
                }
                if (container.getSize() != null) {
                    responseDto.setSize(container.getSize());
                }
                if (container.getState() != null) {
                    responseDto.setState(container.getState());
                }

                containerResponseList.add(responseDto);
            }
            dockerCacheDataService.setContainerCacheData(containerResponseList);

            // Image
            List<ImageResponseDto> imageResponseList = new ArrayList<>();
            for (ImageDto image : systemData.getImageDtoList()) {
                ImageResponseDto responseDto = new ImageResponseDto();

                if (image.getId() != null) {
                    responseDto.setImageId(image.getId());
                }
                if (image.getImageName() != null) {
                    responseDto.setImageName(image.getImageName());
                }
                if (image.getImageVersion() != null) {
                    responseDto.setImageVersion(image.getImageVersion());
                }
                if (image.getCreatedAt() != null) {
                    responseDto.setCreatedAt(image.getCreatedAt());
                }
                if (image.getUsedContainerCount() != null) {
                    responseDto.setUsedContainerCount(image.getUsedContainerCount());
                }
                if (image.getSize() != null) {
                    responseDto.setSize(image.getSize());
                }

                imageResponseList.add(responseDto);
            }
            dockerCacheDataService.setImageCacheData(imageResponseList);

            //Volume
            List<VolumeResponseDto> volumeResponseList = new ArrayList<>();
            for (VolumeDto volume : systemData.getVolumeDtoList()) {
                VolumeResponseDto responseDto = new VolumeResponseDto();

                if (volume.getVolumeName() != null) {
                    responseDto.setVolumeName(volume.getVolumeName());
                }
                if (volume.getContainerCount() != null) {
                    responseDto.setContainerCount(volume.getContainerCount());
                }
                if (volume.getDriver() != null) {
                    responseDto.setDriver(volume.getDriver());
                }
                if (volume.getScope() != null) {
                    responseDto.setScope(volume.getScope());
                }
                if (volume.getSize() != null) {
                    responseDto.setSize(volume.getSize());
                }
                if (volume.getMountPoint() != null) {
                    responseDto.setMountPoint(volume.getMountPoint());
                }
                volumeResponseList.add(responseDto);
            }
            dockerCacheDataService.setVolumeCacheData(volumeResponseList);
        }

        // System Info
        if (infoResponse.isSuccess()) {

            SystemInfoDto infoRawData = null;
            SystemInfoResponseDto infoResponseData = new SystemInfoResponseDto();
            if (infoResponse.getData() != null) {
                infoRawData = (SystemInfoDto) infoResponse.getData();
            }
            if (infoRawData.getDockerId() != null) {
                infoResponseData.setDockerId(infoRawData.getDockerId());
            }
            if (infoRawData.getContainerCount() != null) {
                infoResponseData.setContainerCount(infoRawData.getContainerCount());
            }
            if (infoRawData.getImageCount() != null) {
                infoResponseData.setImageCount(infoRawData.getImageCount());
            }
            if (infoRawData.getFileDescriptorCount() != null) {
                infoResponseData.setFileDescriptorCount(infoRawData.getFileDescriptorCount());
            }
            if (infoRawData.getMemTotal() != null) {
                infoResponseData.setMemTotal(infoRawData.getMemTotal());
            }
            infoResponseData.setMemLimit(infoRawData.isMemLimit());
            infoResponseData.setPidLimit(infoRawData.isPidLimit());
            infoResponseData.setCpuPeriodLimit(infoRawData.isCpuPeriodLimit());
            infoResponseData.setCpuQuotaLimit(infoRawData.isCpuQuotaLimit());
            infoResponseData.setDebugEnable(infoRawData.isDebugEnable());
            infoResponseData.setOutOfMemoryKill(infoRawData.isOutOfMemoryKill());

            if (infoRawData.getKernelVersion() != null) {
                infoResponseData.setKernelVersion(infoRawData.getKernelVersion());
            }
            if (infoRawData.getOperatingSystem() != null) {
                infoResponseData.setOperatingSystem(infoRawData.getOperatingSystem());
            }
            if (infoRawData.getOsVersion() != null) {
                infoResponseData.setOsVersion(infoRawData.getOsVersion());
            }
            if (infoRawData.getOsType() != null) {
                infoResponseData.setOsType(infoRawData.getOsType());
            }
            if (infoRawData.getArchitecture() != null) {
                infoResponseData.setArchitecture(infoRawData.getArchitecture());
            }
            if (infoRawData.getDockerRootDir() != null) {
                infoResponseData.setDockerRootDir(infoRawData.getDockerRootDir());
            }
            if (infoRawData.getServerVersion() != null) {
                infoResponseData.setServerVersion(infoRawData.getServerVersion());
            }

            SystemInfoResponseDto beforeSystemInfo = (SystemInfoResponseDto) dockerCacheDataService.getSystemInfoCacheData();
            dockerCacheDataService.setSystemInfoCacheData(infoResponseData);
            if (beforeSystemInfo != null) {
                boolean difference = SystemInfoResponseDto.isDifferent(beforeSystemInfo, infoResponseData);
                if (difference) {
                    this.dockerService.notifyTotal(new DockerResponseDto(true, "System information fluctuation", null), DockerWebSocketResponse.DataType.SYSTEM);
                }
            }
        }
    }

    //    @Async("threadPoolTaskExecutor")
    public void triggerCollect() {
        collect(); // 특정 이벤트 발생시 임의 메소드 호출
    }

}
