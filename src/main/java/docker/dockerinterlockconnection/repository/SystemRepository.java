package docker.dockerinterlockconnection.repository;

import docker.dockerinterlockconnection.entity.SystemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRepository extends JpaRepository<SystemInfo, String> {
}
