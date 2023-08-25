package docker.dockerinterlockconnection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY) // 빈 문자열 또는 null인 경우 무시
public class SystemInfoDto {
    @JsonProperty("ID")
    private Integer dockerId;

    @JsonProperty("Containers")
    private Integer containerCount;
    @JsonProperty("Images")
    private Integer imageCount;
    @JsonProperty("NFd")
    private Integer fileDescriptorCount;
    @JsonProperty("MemTotal")
    private Long memTotal;

    @JsonProperty("MemoryLimit")
    private boolean memLimit;
    @JsonProperty("CpuCfsPeriod")
    private boolean cpuPeriodLimit;
    @JsonProperty("CpuCfsQuota")
    private boolean cpuQuotaLimit;
    @JsonProperty("PidsLimit")
    private boolean pidLimit;
    @JsonProperty("Debug")
    private boolean debugEnable;
    @JsonProperty("OomKillDisable")
    private boolean OutOfMemoryKill;

    @JsonProperty("KernelVersion")
    private String kernelVersion;
    @JsonProperty("OperatingSystem")
    private String operatingSystem;
    @JsonProperty("OSVersion")
    private String osVersion;
    @JsonProperty("OSType")
    private String osType;
    @JsonProperty("Architecture")
    private String architecture;
    @JsonProperty("DockerRootDir")
    private String dockerRootDir;
    @JsonProperty("ServerVersion")
    private String serverVersion;



}
