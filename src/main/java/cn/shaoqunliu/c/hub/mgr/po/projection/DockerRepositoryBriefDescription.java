package cn.shaoqunliu.c.hub.mgr.po.projection;

public interface DockerRepositoryBriefDescription {

    Integer getId();

    DockerNamespaceWithoutOwner getNamespace();

    String getName();

    Long getStars();

    String getDescription();
}
