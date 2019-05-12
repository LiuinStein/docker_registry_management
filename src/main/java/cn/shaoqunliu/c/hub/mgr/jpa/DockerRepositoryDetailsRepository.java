package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerRepositoryDetailsRepository extends JpaRepository<DockerRepository, Integer> {

    DockerRepositoryBasic getDockerRepositoryBasicByNamespaceNameAndName(String namespace, String name);

    DockerRepository getDockerRepositoryByNamespaceNameAndName(String namespace, String name);
}
