package docker.dockerinterlockconnection.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserCacheDataService {

    private String authorizationData;
    private LocalDateTime authorizationDataTimestamp; // 저장된 시간

    @Getter
    private static final UserCacheDataService userCacheDataService = new UserCacheDataService();

    public UserCacheDataService() {

    }
    public void setAuthorizationData(String authorizationData) {
        this.authorizationData = authorizationData;
        this.authorizationDataTimestamp = LocalDateTime.now(); // 값이 들어온 시간 저장
    }

    @Scheduled(fixedRate = 4 * 60 * 60 * 1000) // 4시간마다 실행 (단위: 밀리초)
    public void resetAuthorizationData() {
        if (authorizationDataTimestamp != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expirationTime = authorizationDataTimestamp.plusHours(4); // 들어온 시간 + 4시간
            if (currentTime.isAfter(expirationTime)) {
                // 4시간 이후에 초기화
                deleteAuthorizationData();
                this.authorizationDataTimestamp = null;
            }
        }
    }

    public void deleteAuthorizationData() {
        this.authorizationData = null;
    }

    public String getAuthorizationData() {
        return this.authorizationData;
    }


}
