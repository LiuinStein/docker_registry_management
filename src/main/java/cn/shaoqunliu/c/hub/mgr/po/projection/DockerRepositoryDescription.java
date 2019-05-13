package cn.shaoqunliu.c.hub.mgr.po.projection;

public interface DockerRepositoryDescription {

    Integer getId();

    Boolean getOpened();

    Long getStars();

    String getDescription();

    String getFullDescription();

    DockerNamespaceWithoutOwner getNamespace();

    String getName();
}
