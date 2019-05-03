package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;

public interface DockerRepositoryService {

    DockerRepository getDockerRepositoryByIdentifier(String namespace, String repository);

    DockerRepository save(DockerRepository repository);
}
