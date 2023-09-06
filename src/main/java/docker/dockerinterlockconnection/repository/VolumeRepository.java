package docker.dockerinterlockconnection.repository;

import docker.dockerinterlockconnection.entity.Volume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, String> {
}
