package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerNamespaceWithoutOwner;
import org.springframework.data.domain.Page;

public interface DockerNamespaceService {

    DockerNamespace getDockerNamespaceByName(String name);

    Page<DockerNamespaceWithoutOwner> getNamespacesByOwner(int uid, int page);

    DockerNamespace addNamespaceIfNotExists(String namespace, int uid) throws ResourceNeedCreatedAlreadyExistsException;
}
