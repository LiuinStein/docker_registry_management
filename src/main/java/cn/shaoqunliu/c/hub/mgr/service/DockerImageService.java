package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.DockerImage;

public interface DockerImageService {

    DockerImage updateIfExistsAndAddIfNot(DockerImage image);
}
