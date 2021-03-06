package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasic;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasicWithoutOwner;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBriefDescription;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryDescription;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DockerRepositoryService {

    DockerRepositoryBasic getDockerRepositoryBasicByIdentifier(String namespace, String repository);

    DockerRepositoryDescription getDockerRepositoryDescriptionByIdentifier(String namespace, String repository);

    Page<DockerRepositoryBriefDescription> getBriefDescriptionByNamespace(String namespace, int page);

    Page<DockerRepositoryBriefDescription> getPublicRepositoryBriefDescription(int page);

    Page<DockerRepositoryBriefDescription> getBriefDescriptionByFuzzyName(String name, int page);

    Page<DockerRepositoryBriefDescription> getBriefDescriptionByOwner(int uid, int page);

    DockerRepository save(DockerRepository repository);

    Integer update(DockerImageIdentifier identifier,
                   DockerRepository details);

    void deleteByIdentifier(DockerImageIdentifier identifier);

    boolean isOpened(DockerImageIdentifier identifier);
}
