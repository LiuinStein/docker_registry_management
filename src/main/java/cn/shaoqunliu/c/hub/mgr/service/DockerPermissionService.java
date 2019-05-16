package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.security.details.Action;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;

public interface DockerPermissionService {

    void changeOwnerOfRepository(DockerImageIdentifier identifier, int namespaceOwner, String newOwner) throws ResourceNotFoundException;

    void changeDockerPermissions(DockerImageIdentifier identifier, String user, Action action) throws ResourceNotFoundException;
}
