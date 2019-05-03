package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.DockerUserRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import cn.shaoqunliu.c.hub.mgr.service.DockerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("myDockerUserService")
public class MyDockerUserService implements DockerUserService {

    private final DockerUserRepository userRepository;

    @Autowired
    public MyDockerUserService(DockerUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public DockerUser getUserByName(String identifier) {
        Objects.requireNonNull(identifier);
        if (identifier.indexOf('@') != -1) {
            return userRepository.getDockerUserByEmail(identifier);
        }
        return userRepository.getDockerUserByUsername(identifier);
    }
}
