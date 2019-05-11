package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerNamespaceRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerNamespaceWithoutOwner;
import cn.shaoqunliu.c.hub.mgr.service.DockerNamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public Page<DockerNamespaceWithoutOwner> getNamespacesByOwner(int uid, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        DockerUser user = new DockerUser();
        user.setId(uid);
        return namespaceRepository.findAllByOwner(user, pageRequest);
    }

    @Override
    public DockerNamespace addNamespaceIfNotExists(String namespace, int uid) throws ResourceNeedCreatedAlreadyExistsException {
        if (namespaceRepository.existsByName(namespace)) {
            throw new ResourceNeedCreatedAlreadyExistsException(
                    "the namespace " + namespace + " already exists"
            );
        }
        DockerNamespace dockerNamespace = new DockerNamespace();
        dockerNamespace.setName(namespace);
        DockerUser owner = new DockerUser();
        owner.setId(uid);
        dockerNamespace.setOwner(owner);
        return namespaceRepository.save(dockerNamespace);
    }
}