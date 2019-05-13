package cn.shaoqunliu.c.hub.mgr.service;

import cn.shaoqunliu.c.hub.mgr.po.DockerImage;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerImageBasic;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.data.domain.Page;

public interface DockerImageService {

    DockerImage updateIfExistsAndAddIfNot(DockerImage image);

    Page<DockerImageBasic> getImagesFromRepository(DockerImageIdentifier identifier, int page);
}
