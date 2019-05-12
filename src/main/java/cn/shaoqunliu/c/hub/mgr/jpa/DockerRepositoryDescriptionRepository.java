package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerRepositoryDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerRepositoryDescriptionRepository extends JpaRepository<DockerRepositoryDescription, Integer> {

    @Query("SELECT drd FROM  DockerRepositoryDescription drd WHERE namespace.id=(SELECT id FROM DockerNamespace WHERE name=:namespace) AND name=:repository")
    DockerRepositoryDescription getDockerRepositoryDescriptionByIdentifier(
            @Param("namespace") String namespace,
            @Param("repository") String repository);
}
