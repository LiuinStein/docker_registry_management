package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;

public interface DockerNamespaceService {

    DockerNamespace getDockerNamespaceByName(String name);
}
