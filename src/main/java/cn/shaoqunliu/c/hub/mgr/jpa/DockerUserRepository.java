package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockerUserRepository extends JpaRepository<DockerUser, Integer> {

    DockerUser getDockerUserByUsername(String username);

    DockerUser getDockerUserByEmail(String email);
}
