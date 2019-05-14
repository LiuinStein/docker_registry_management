package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerStars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerStarRepository extends JpaRepository<DockerStars, DockerStars.Pk> {

    Boolean existsByPkUidAndPkRid(Integer uid, Integer rid);
}
