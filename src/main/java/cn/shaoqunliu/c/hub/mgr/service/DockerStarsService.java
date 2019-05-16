package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.exception.PageNumberOutOfRangeException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBriefDescription;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;

import java.util.List;


public interface DockerStarsService {

    void starRepository(DockerImageIdentifier identifier, int uid);

    void unStarRepository(DockerImageIdentifier identifier, int uid);

    boolean ifStarred(DockerImageIdentifier identifier, int uid);

    List<DockerRepositoryBriefDescription> getStarredRepositoriesByUserId(int uid) throws ResourceNotFoundException;
}
