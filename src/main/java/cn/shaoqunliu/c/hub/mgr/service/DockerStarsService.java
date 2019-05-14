package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;

public interface DockerStarsService {

    void starRepository(DockerImageIdentifier identifier, int uid);

    void unStarRepository(DockerImageIdentifier identifier, int uid);

    boolean ifStarred(DockerImageIdentifier identifier, int uid);
}
