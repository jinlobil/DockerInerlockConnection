package docker.dockerinterlockconnection.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "volume")
public class Volume {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "docker_id")
    private SystemInfo systemInfo;

    @Column(name = "name")
    private String volumeName;

    @Column(name = "driver")
    private String driver;

    @Column(name = "mount")
    private String mountPoint;

    @Column(name = "container_count")
    private Integer containerCount;

    @Column(name = "scope")
    private String scope;

    @Column(name = "size")
    private String size;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "label")
    private String labels;

    @Column(name = "used_container_name")
    private String containerNames;

}
