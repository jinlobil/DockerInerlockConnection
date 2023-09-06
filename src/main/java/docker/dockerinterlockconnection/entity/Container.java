package docker.dockerinterlockconnection.entity;

import docker.dockerinterlockconnection.dto.response.ContainerMountResponseDto;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "container")
public class Container {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "docker_id")
    private SystemInfo systemInfo;

    @Column(name = "name")
    private String containerName;

    @Column(name = "volume_count")
    private Integer volumeCount;

    @Column(name = "state")
    private String state;

    @Column(name = "port")
    private String ports;

    @Column(name = "mount")
    private String mounts;

    @Column(name = "size")
    private String size;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "created_at")
    private String createdAt;


}
