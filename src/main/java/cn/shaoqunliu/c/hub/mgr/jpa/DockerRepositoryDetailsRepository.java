package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasic;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBriefDescription;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerRepositoryDetailsRepository extends PagingAndSortingRepository<DockerRepository, Integer> {

    DockerRepositoryBasic getDockerRepositoryBasicByNamespaceNameAndName(String namespace, String name);

    DockerRepositoryDescription getDockerRepositoryDescriptionByNamespaceNameAndName(String namespace, String name);

    DockerRepository getDockerRepositoryByNamespaceNameAndName(String namespace, String name);

    Page<DockerRepositoryBriefDescription> findAllByNamespaceNameOrderByStarsDesc(String namespace, Pageable pageable);
}
