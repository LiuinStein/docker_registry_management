package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerNamespaceRepository extends JpaRepository<DockerNamespace, Integer> {

    DockerNamespace getDockerNamespaceByName(String name);

    Boolean existsByName(String name);
}
