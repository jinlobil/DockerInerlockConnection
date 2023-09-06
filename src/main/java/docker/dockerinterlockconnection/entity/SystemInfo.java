package docker.dockerinterlockconnection.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "docker")
public class SystemInfo {
    @Id
    private String id;

    @Column(name = "kernel_version")
    private String kernelVersion;

    @Column(name = "operating_system")
    private String operatingSystem;

    @Column(name = "os_version")
    private String osVersion;

    @Column(name = "os_type")
    private String osType;

    @Column(name = "architecture")
    private String architecture;

    @Column(name = "docker_root_dir")
    private String dockerRootDir;

    @Column(name = "server_version")
    private String serverVersion;

}
