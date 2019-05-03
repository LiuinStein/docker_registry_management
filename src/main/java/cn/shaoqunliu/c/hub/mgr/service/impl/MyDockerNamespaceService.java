package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.DockerNamespaceRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.service.DockerNamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("myDockerNamespaceService")
public class MyDockerNamespaceService implements DockerNamespaceService {

    private final DockerNamespaceRepository namespaceRepository;

    @Autowired
    public MyDockerNamespaceService(DockerNamespaceRepository namespaceRepository) {
        this.namespaceRepository = namespaceRepository;
    }

    @Override
    public DockerNamespace getDockerNamespaceByName(String name) {
        Objects.requireNonNull(name);
        return namespaceRepository.getDockerNamespaceByName(name);
    }
}
