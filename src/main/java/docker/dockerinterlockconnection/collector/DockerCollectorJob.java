package docker.dockerinterlockconnection.collector;

import docker.dockerinterlockconnection.dto.*;
import docker.dockerinterlockconnection.dto.response.*;
import docker.dockerinterlockconnection.service.DockerCacheDataService;
import docker.dockerinterlockconnection.service.DockerService;
import docker.dockerinterlockconnection.service.SystemService;
import docker.dockerinterlockconnection.websocket.DockerWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class DockerCollectorJob {
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final DockerWebSocketHandler dockerWebSocketHandler;
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
            for (ContainerDto containerData : systemData.getContainerDtoList()) {
                ContainerResponseDto responseDto = new ContainerResponseDto();
                DockerResponseDto containerInspectData = new DockerResponseDto();
                ContainerInspectResponseDto containerInspect = new ContainerInspectResponseDto();

                if (containerData.getContainerId() != null) {
                    containerInspect.setContainerId(containerData.getContainerId());
                    containerInspectData = this.systemService.getContainerInspect(containerData.getContainerId());
                }

                containerInspect.setCreatedAt(containerData.getCreatedAt() != null ? containerData.getCreatedAt() : null);
                containerInspect.setImageId(containerData.getImageId() != null ? containerData.getImageId() : null);
                containerInspect.setSize(containerData.getSize() != null ? containerData.getSize() : null);

                if (containerInspectData.isSuccess()) {
                    ContainerInspectDto containerInspectDto = (ContainerInspectDto) containerInspectData.getData();
                    ArrayList portList;
                    if (containerInspectDto.getMounts() != null) {
                        portList = new ArrayList();
                        for (ContainerMountsDto mount : containerInspectDto.getMounts()) {

                            ContainerMountResponseDto containerMountResponse = new ContainerMountResponseDto();

                            containerMountResponse.setType(mount.getType() != null ? mount.getType() : null);
                            containerMountResponse.setName(mount.getName() != null ? mount.getName() : null);
                            containerMountResponse.setSource(mount.getSource() != null ? mount.getSource() : null);
                            containerMountResponse.setDestination(mount.getDestination() != null ? mount.getDestination() : null);
                            containerMountResponse.setDriver(mount.getDriver() != null ? mount.getDriver() : null);
                            containerMountResponse.setMode(mount.getMode() != null ? mount.getMode() : null);
                            containerMountResponse.setRw(mount.getRw());

                            portList.add(containerMountResponse);

                        }
                        containerInspect.setMounts(portList);
                    }

                    if (containerInspectDto.getHostConfig().getPortBind() != null) {
                        portList = new ArrayList();
                        String extractedNumber = null;
                        for (Map.Entry<String, Object> map : containerInspectDto.getHostConfig().getPortBind().getPortsMap().entrySet()) {
                            String port = map.getKey();
                            if (map.getValue() != null) {
                                // getValue = [{HostIp=, HostPort=8080}]
                                String ports = map.getValue().toString();
                                String[] portSplit = ports.split(",");
                                String portTwo = portSplit[1];
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(portTwo);
                                if (matcher.find()) {
                                    extractedNumber = matcher.group(); // 추출된 숫자
                                }

                                //                String portInfo = port + "=" + extractedNumber;
                                String portInfo = extractedNumber + ":" + port;
                                portList.add(portInfo);
                            }
                        }
                        containerInspect.setPorts(portList);
                    }
                }

                responseDto.setInspect(containerInspect);

                responseDto.setVolumeCount(containerData.getVolumeCount() != null ? containerData.getVolumeCount() : null);
                responseDto.setContainerName(containerData.getContainerName() != null ? containerData.getContainerName() : null);
                responseDto.setState(containerData.getState() != null ? containerData.getState() : null);
                containerResponseList.add(responseDto);
            }
            dockerCacheDataService.setContainerCacheData(containerResponseList);

            // Image
            List<ImageResponseDto> imageResponseList = new ArrayList<>();
            for (ImageDto imageData : systemData.getImageDtoList()) {
                ImageResponseDto responseDto = new ImageResponseDto();
                ImageInspectResponseDto imageInspect = new ImageInspectResponseDto();
                DockerResponseDto imageInspectData = new DockerResponseDto();

                if (imageData.getId() != null) {
                    imageInspect.setImageId(imageData.getId());
                    imageInspectData = this.systemService.getLocalImageInspect(imageData.getId());
                }

                imageInspect.setCreatedAt(imageData.getCreatedAt() != null ? imageData.getCreatedAt() : null);
                imageInspect.setSize(imageData.getSize() != null ? imageData.getSize() : null);

                if (imageInspectData.isSuccess()) {
                    ImageInspectDto imageInspectDto = (ImageInspectDto) imageInspectData.getData();

                    imageInspect.setArchitecture(imageInspectDto.getArchitecture() != null ? imageInspectDto.getArchitecture() : null);
                    imageInspect.setOs(imageInspectDto.getOs() != null ? imageInspectDto.getOs() : null);
                    imageInspect.setEnv(imageInspectDto.getConfig().getEnv() != null ? imageInspectDto.getConfig().getEnv() : null);
                }
                responseDto.setInspect(imageInspect);

                responseDto.setImageName(imageData.getImageName() != null ? imageData.getImageName() : null);
                responseDto.setImageVersion(imageData.getImageVersion() != null ? imageData.getImageVersion() : null);
                responseDto.setContainerCount(imageData.getUsedContainerCount() != null ? Integer.valueOf(imageData.getUsedContainerCount()) : null);

                imageResponseList.add(responseDto);
            }
            this.dockerCacheDataService.setImageCacheData(imageResponseList);

            //Volume
            List<VolumeResponseDto> volumeResponseList = new ArrayList<>();
            for (VolumeDto volumeData : systemData.getVolumeDtoList()) {
                VolumeResponseDto responseDto = new VolumeResponseDto();
                DockerResponseDto volumeInspectData = new DockerResponseDto();
                VolumeInspectResponseDto volumeInspect = new VolumeInspectResponseDto();

                if (volumeData.getVolumeName() != null) {
                    responseDto.setVolumeName(volumeData.getVolumeName());
                    volumeInspectData = this.systemService.getVolumeInspect(volumeData.getVolumeName());
                }
                responseDto.setContainerCount(volumeData.getContainerCount() != null ? volumeData.getContainerCount() : null);
                responseDto.setMountPoint(volumeData.getMountPoint() != null ? volumeData.getMountPoint() : null);

                if (volumeInspectData.isSuccess()) {
                    VolumeInspectDto volumeInspectDto = (VolumeInspectDto)volumeInspectData.getData();
                    if (volumeInspectDto.getLabels() != null) {
                        List<String> labelList = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : volumeInspectDto.getLabels().entrySet()) {
                            String keyValueString = entry.getKey() + "=" + entry.getValue();
                            labelList.add(keyValueString);
                        }
                        volumeInspect.setLabels(labelList);
                    }
                    volumeInspect.setContainerNames(volumeInspectDto.getUseContainerNames() != null ? volumeInspectDto.getUseContainerNames(): null);
                    volumeInspect.setCreatedAt(volumeInspectDto.getCreatedAt() != null ? volumeInspectDto.getCreatedAt() : null);
                }
                volumeInspect.setDriver(volumeData.getDriver() != null ? volumeData.getDriver() : null);
                volumeInspect.setScope(volumeData.getScope() != null ? volumeData.getScope() : null);
                volumeInspect.setSize(volumeData.getSize() != null ? volumeData.getSize() : null);

                responseDto.setInspect(volumeInspect);

                volumeResponseList.add(responseDto);
            }
            this.dockerCacheDataService.setVolumeCacheData(volumeResponseList);
        }

        // System Info
        if (infoResponse.isSuccess()) {

            SystemInfoDto infoRawData = null;
            SystemInfoResponseDto infoResponseData = new SystemInfoResponseDto();
            if (infoResponse.getData() != null) {
                infoRawData = (SystemInfoDto) infoResponse.getData();

                infoResponseData.setDockerId(infoRawData.getDockerId() != null ? infoRawData.getDockerId() : null);
                infoResponseData.setContainerCount(infoRawData.getContainerCount() != null ? infoRawData.getContainerCount() : null);
                infoResponseData.setImageCount(infoRawData.getImageCount() != null ? infoRawData.getImageCount() : null);
                infoResponseData.setFileDescriptorCount(infoRawData.getFileDescriptorCount() != null ? infoRawData.getFileDescriptorCount() : null);
                infoResponseData.setMemTotal(infoRawData.getMemTotal() != null ? infoRawData.getMemTotal() : null);

                infoResponseData.setMemLimit(infoRawData.isMemLimit());
                infoResponseData.setPidLimit(infoRawData.isPidLimit());
                infoResponseData.setCpuPeriodLimit(infoRawData.isCpuPeriodLimit());
                infoResponseData.setCpuQuotaLimit(infoRawData.isCpuQuotaLimit());
                infoResponseData.setDebugEnable(infoRawData.isDebugEnable());
                infoResponseData.setOutOfMemoryKill(infoRawData.isOutOfMemoryKill());

                infoResponseData.setKernelVersion(infoRawData.getKernelVersion() != null ? infoRawData.getKernelVersion() : null);
                infoResponseData.setOperatingSystem(infoRawData.getOperatingSystem() != null ? infoRawData.getOperatingSystem() : null);
                infoResponseData.setOsVersion(infoRawData.getOsVersion() != null ? infoRawData.getOsVersion() : null);
                infoResponseData.setOsType(infoRawData.getOsType() != null ? infoRawData.getOsType() : null);
                infoResponseData.setArchitecture(infoRawData.getArchitecture() != null ? infoRawData.getArchitecture() : null);
                infoResponseData.setDockerRootDir(infoRawData.getDockerRootDir() != null ? infoRawData.getDockerRootDir() : null);
                infoResponseData.setServerVersion(infoRawData.getServerVersion() != null ? infoRawData.getServerVersion() : null);
            }

            SystemInfoResponseDto beforeSystemInfo = (SystemInfoResponseDto) dockerCacheDataService.getSystemInfoCacheData();
            dockerCacheDataService.setSystemInfoCacheData(infoResponseData);
            if (beforeSystemInfo != null) {
                boolean difference = SystemInfoResponseDto.isDifferent(beforeSystemInfo, infoResponseData);
                if (difference) {
                    dockerWebSocketHandler.broadcast(true, "System information fluctuation", infoResponseData, DockerWebSocketResponse.RequestType.INFO, DockerWebSocketResponse.DataType.SYSTEM);
//                    this.dockerService.notifyTotal(new DockerResponseDto(true, "System information fluctuation", null), DockerWebSocketResponse.DataType.SYSTEM);
                }
            }
        }
    }

    //    @Async("threadPoolTaskExecutor")
    public void triggerCollect() {
        collect(); // 특정 이벤트 발생시 임의 메소드 호출
    }

}
