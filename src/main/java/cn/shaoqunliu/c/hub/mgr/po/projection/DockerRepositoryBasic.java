package cn.shaoqunliu.c.hub.mgr.po.projection;

import cn.shaoqunliu.c.hub.mgr.po.DockerUser;

public interface DockerRepositoryBasic {

    Integer getId();

    DockerNamespaceWithoutOwner getNamespace();

    String getName();

    DockerUser getOwner();

    Boolean getOpened();
}
