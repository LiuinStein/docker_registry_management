package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerImage;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerImageBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerImageRepository extends PagingAndSortingRepository<DockerImage, Integer> {

    DockerImageBasic getDockerImageByRepositoryAndName(DockerRepository repository, String name);

    Page<DockerImageBasic> findAllByRepositoryNamespaceNameAndRepositoryName(String namespace, String repository, Pageable pageable);
}
