package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerImage;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerImageRepository extends JpaRepository<DockerImage, Integer> {

    DockerImage getDockerImageByRepositoryAndName(DockerRepository repository, String name);
}
