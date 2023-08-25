package docker.dockerinterlockconnection.dto.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DockerWebSocketResponse {
    private boolean isSuccess;
    private String message;
    private Object data;

    private RequestType requestType;
    private DataType dataType;

    public enum RequestType {
        INFO(0),               // 현재 상태(최신 데이터)를 알려줄 때
        ALARM(1);              // 요청에 대한 결과를 알려줄 때

        @JsonValue
        final int num;

        RequestType(int num) {
            this.num = num;
        }
    }

    public enum DataType {

        CONTAINER(0),
        IMAGE(1),
        VOLUME(2),
        SYSTEM(3);

        @JsonValue
        final int num;

        DataType(int num) {
            this.num = num;
        }
    }
}
