package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;

public interface DockerNamespaceService {

    DockerNamespace getDockerNamespaceByName(String name);

    DockerNamespace addNamespaceIfNotExists(String namespace, int uid) throws ResourceNeedCreatedAlreadyExistsException;
}
