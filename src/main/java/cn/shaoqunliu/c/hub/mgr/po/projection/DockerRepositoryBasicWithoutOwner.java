package cn.shaoqunliu.c.hub.mgr.po.projection;

public interface DockerRepositoryBasicWithoutOwner {

    Integer getId();

    DockerNamespaceWithoutOwner getNamespace();

    String getName();

    Boolean getOpened();
}
