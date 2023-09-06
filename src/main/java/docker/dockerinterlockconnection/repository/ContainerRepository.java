package docker.dockerinterlockconnection.repository;

import docker.dockerinterlockconnection.entity.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContainerRepository extends JpaRepository<Container, String> {
}
