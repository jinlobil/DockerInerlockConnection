package docker.dockerinterlockconnection.dto.response;

import lombok.Data;

import java.util.Objects;

@Data
public class SystemInfoResponseDto {
    private Integer dockerId;

    private Integer containerCount;
    private Integer imageCount;
    private Integer fileDescriptorCount;
    private Long memTotal;

    private boolean memLimit;
    private boolean pidLimit;
    private boolean cpuPeriodLimit;
    private boolean cpuQuotaLimit;
    private boolean debugEnable;
    private boolean OutOfMemoryKill;

    private String kernelVersion;
    private String operatingSystem;
    private String osVersion;
    private String osType;
    private String architecture;
    private String dockerRootDir;
    private String serverVersion;

    public static boolean isDifferent(SystemInfoResponseDto before, SystemInfoResponseDto after) {
        return !Objects.equals(before.getDockerId(), after.getDockerId())
                || !Objects.equals(before.getContainerCount(), after.getContainerCount())
                || !Objects.equals(before.getImageCount(), after.getImageCount())
                || !Objects.equals(before.getFileDescriptorCount(), after.getFileDescriptorCount())
                || !Objects.equals(before.getMemTotal(), after.getMemTotal())
                || before.isMemLimit() != after.isMemLimit()
                || before.isPidLimit() != after.isPidLimit()
                || before.isCpuPeriodLimit() != after.isCpuPeriodLimit()
                || before.isCpuQuotaLimit() != after.isCpuQuotaLimit()
                || before.isDebugEnable() != after.isDebugEnable()
                || before.isOutOfMemoryKill() != after.isOutOfMemoryKill()
                || !Objects.equals(before.getKernelVersion(), after.getKernelVersion())
                || !Objects.equals(before.getOperatingSystem(), after.getOperatingSystem())
                || !Objects.equals(before.getOsVersion(), after.getOsVersion())
                || !Objects.equals(before.getOsType(), after.getOsType())
                || !Objects.equals(before.getArchitecture(), after.getArchitecture())
                || !Objects.equals(before.getDockerRootDir(), after.getDockerRootDir())
                || !Objects.equals(before.getServerVersion(), after.getServerVersion());
    }
}
