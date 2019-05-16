package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import cn.shaoqunliu.c.hub.mgr.po.projection.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DockerRepositoryDetailsRepository extends PagingAndSortingRepository<DockerRepository, Integer> {

    DockerRepositoryBriefDescription getDockerRepositoryBriefDescriptionById(Integer id);

    DockerRepositoryBasic getDockerRepositoryBasicByNamespaceNameAndName(String namespace, String name);

    DockerRepositoryDescription getDockerRepositoryDescriptionByNamespaceNameAndName(String namespace, String name);

    DockerRepositoryStars getDockerRepositoryStarsByNamespaceNameAndName(String namespace, String name);

    DockerRepository getDockerRepositoryByNamespaceNameAndName(String namespace, String name);

    List<DockerRepositoryBasicWithoutOwner> findAllByNamespaceName(String namespace);

    Page<DockerRepositoryBriefDescription> findAllByNamespaceNameOrderByStarsDesc(String namespace, Pageable pageable);

    Page<DockerRepositoryBriefDescription> findAllByOpenedIsTrueOrderByStarsDesc(Pageable pageable);

    Page<DockerRepositoryBriefDescription> findAllByNameContainsOrderByStarsDesc(String name, Pageable pageable);

    Page<DockerRepositoryBriefDescription> findAllByOwnerOrderByStarsDesc(DockerUser user, Pageable pageable);

    @Modifying
    @Query("UPDATE DockerRepository dr SET dr.stars=dr.stars+?1 WHERE id=?2")
    void starOnce(Long num, Integer rid);

    @Modifying
    @Query("UPDATE DockerRepository dr SET dr.owner=?1 WHERE dr.id=?2")
    void changeOwner(DockerUser owner, Integer id);
}
