package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.DockerUser;

public interface DockerUserService {

    DockerUser getUserByName(String identifier);
}
