package cn.shaoqunliu.c.hub.mgr.po.projection;

import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;

public interface DockerRepositoryDescription {

    Integer getId();

    Boolean getOpened();

    Long getStars();

    String getDescription();

    String getFullDescription();

    DockerNamespace getNamespace();

    String getName();
}
