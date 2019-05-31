package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerPermission;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerPermissionBasic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerPermissionRepository extends PagingAndSortingRepository<DockerPermission, Integer> {

    Page<DockerPermissionBasic> findAllByRepositoryNamespaceNameAndRepositoryName(String namespace, String repository, Pageable pageable);

    DockerPermission getByUserIdAndRepositoryId(Integer uid, Integer rid);

    void deleteByUserUsernameAndRepositoryNamespaceNameAndRepositoryName(String username, String namespace, String repository);
}
