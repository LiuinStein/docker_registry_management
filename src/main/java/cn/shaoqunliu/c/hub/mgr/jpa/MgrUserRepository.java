package cn.shaoqunliu.c.hub.mgr.jpa;

import cn.shaoqunliu.c.hub.mgr.po.MgrUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MgrUserRepository extends JpaRepository<MgrUser, Integer> {

    Boolean existsByUsernameOrEmail(String username, String email);
}
