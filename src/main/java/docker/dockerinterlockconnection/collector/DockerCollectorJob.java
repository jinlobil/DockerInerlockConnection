package docker.dockerinterlockconnection.collector;

import docker.dockerinterlockconnection.dto.DockerResponseDto;
import docker.dockerinterlockconnection.dto.SystemResponseDto;
import docker.dockerinterlockconnection.service.DockerCacheDataService;
import docker.dockerinterlockconnection.service.SystemService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DockerCollectorJob {
    private final DockerCacheDataService dockerCacheDataService = DockerCacheDataService.getDockerCacheDataService();
    private final SystemService systemService;


    @Async("threadPoolTaskExecutor")
    //fixedDelay는 해당 작업이 끝난 시점부터 시간을 세고, fixedRate는 해당 작업의 시작 시점부터 시간을 세기 때문
    @Scheduled(initialDelay = 1000, fixedRate = 10000) // 5초마다 실행
    public void collect() {
        log.info("DockerCollectorJob_collect START");
        System.out.println("DockerCollectorJob_collect START");
        log.info("DockerCollectorJob_collect START on thread: {}", Thread.currentThread().getName());
        DockerResponseDto response = systemService.getSystemInfo();
        if (response.isSuccess()) {
            SystemResponseDto systemResponse = (SystemResponseDto) response.getData();
            dockerCacheDataService.setContainerCacheData(systemResponse.getContainerDtoList());
            dockerCacheDataService.setImageCacheData(systemResponse.getImageDtoList());
            dockerCacheDataService.setVolumeCacheData(systemResponse.getVolumeDtoList());
        }
    }

    @Async("threadPoolTaskExecutor")
    public void triggerCollect() {
        collect(); // 특정 이벤트 발생시 임의 메소드 호출
    }

}
