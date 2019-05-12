package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepositoryDescription;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;

public interface DockerRepositoryService {

    DockerRepository getDockerRepositoryByIdentifier(String namespace, String repository);

    Boolean isOpened(String namespace, String repository);

    DockerRepository save(DockerRepository repository);

    Integer update(DockerImageIdentifier identifier,
                   DockerRepositoryDescription details);
}
