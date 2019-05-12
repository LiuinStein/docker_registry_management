package cn.shaoqunliu.c.hub.mgr.po.projection;

import java.util.Date;

public interface DockerImageBasic {

    Integer getId();

    DockerRepositoryBasic getRepository();

    String getName();

    Long getSize();

    Date getCreated();

    String getSha256();
}
