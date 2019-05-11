package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerNamespaceWithoutOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerNamespaceRepository extends PagingAndSortingRepository<DockerNamespace, Integer> {

    DockerNamespace getDockerNamespaceByName(String name);

    Page<DockerNamespaceWithoutOwner> findAllByOwner(DockerUser owner, Pageable pageable);

    Boolean existsByName(String name);
}
