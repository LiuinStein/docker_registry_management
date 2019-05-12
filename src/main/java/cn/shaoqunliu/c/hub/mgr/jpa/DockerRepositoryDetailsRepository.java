package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerRepositoryDetailsRepository extends JpaRepository<DockerRepository, Integer> {

    @Query("SELECT dr FROM DockerRepository dr WHERE namespace.id=(SELECT id FROM DockerNamespace WHERE name=:namespace) AND name=:repository")
    DockerRepository getDockerRepositoryByIdentifier(@Param("namespace") String namespace,
                                                     @Param("repository") String repository);

    @Query("SELECT dr.opened FROM DockerRepository dr WHERE namespace.id=(SELECT id FROM DockerNamespace WHERE name=:namespace) AND name=:repository")
    Boolean getOpenedByIdentifier(@Param("namespace") String namespace,
                                  @Param("repository") String repository);

    @Query("SELECT dr.id FROM DockerRepository dr WHERE namespace.id=(SELECT id FROM DockerNamespace WHERE name=:namespace) AND name=:repository")
    Integer getIdByIdentifier(@Param("namespace") String namespace,
                              @Param("repository") String repository);
}
