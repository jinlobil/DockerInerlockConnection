package docker.dockerinterlockconnection.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "image")
public class Image {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "docker_id")
    private SystemInfo systemInfo;

    @Column(name = "name")
    private String imageName;

    @Column(name = "version")
    private String imageVersion;

    @Column(name = "container_count")
    private Integer containerCount;

    @Column(name = "size")
    private String size;

    @Column(name = "env")
    private String env;

    @Column(name = "architecture")
    private String architecture;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "os")
    private String os;


}
