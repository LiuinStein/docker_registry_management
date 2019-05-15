package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerPermissionRepository extends JpaRepository<DockerPermission, Integer> {

}
