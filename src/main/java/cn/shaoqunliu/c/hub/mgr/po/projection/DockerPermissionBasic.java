package cn.shaoqunliu.c.hub.mgr.po.projection;

import cn.shaoqunliu.c.hub.mgr.po.DockerUser;

public interface DockerPermissionBasic {

    Integer getId();

    DockerUser getUser();

    DockerRepositoryBasicWithoutOwner getRepository();

    Integer getAction();
}
