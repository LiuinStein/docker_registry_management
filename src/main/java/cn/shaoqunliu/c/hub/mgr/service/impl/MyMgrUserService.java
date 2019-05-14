package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.MgrUserInfoRepository;
import cn.shaoqunliu.c.hub.mgr.jpa.MgrUserRepository;
import cn.shaoqunliu.c.hub.mgr.po.MgrUser;
import cn.shaoqunliu.c.hub.mgr.po.MgrUserInfo;
import cn.shaoqunliu.c.hub.mgr.service.MgrUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service("myMgrUserService")
public class MyMgrUserService implements MgrUserService {

    private final MgrUserRepository userRepository;
    private final MgrUserInfoRepository infoRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyMgrUserService(MgrUserRepository userRepository, MgrUserInfoRepository infoRepository) {
        this.userRepository = userRepository;
        this.infoRepository = infoRepository;
        passwordEncoder = new BCryptPasswordEncoder(11);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Integer signUp(MgrUser user) {
        Objects.requireNonNull(user);
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            // username or email already exists
            return null;
        }
        // set default value
        user.setEnabled(true);
        user.setRole(0);
        // re-set password
        user.setMpassword(passwordEncoder.encode(user.getMpassword()));
        user.setCpassword(passwordEncoder.encode(user.getCpassword()));
        // save in docker_auth table
        Integer id = userRepository.save(user).getId();
        Objects.requireNonNull(id);
        // save default user info
        MgrUserInfo info = new MgrUserInfo();
        info.setId(id);
        info.setGravatar(user.getEmail());
        infoRepository.save(info);
        return id;
    }
}
