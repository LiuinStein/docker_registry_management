package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasic;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryDescription;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;

public interface DockerRepositoryService {

    DockerRepositoryBasic getDockerRepositoryBasicByIdentifier(String namespace, String repository);

    public DockerRepositoryDescription getDockerRepositoryDescriptionByIdentifier(String namespace, String repository);

    DockerRepository save(DockerRepository repository);

    Integer update(DockerImageIdentifier identifier,
                   DockerRepository details);
}
