package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.DockerRepositoryDetailsRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.service.DockerRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("myDockerRepositoryService")
public class MyDockerRepositoryService implements DockerRepositoryService {

    private final DockerRepositoryDetailsRepository repositoryDetailsRepository;

    @Autowired
    public MyDockerRepositoryService(DockerRepositoryDetailsRepository repositoryDetailsRepository) {
        this.repositoryDetailsRepository = repositoryDetailsRepository;
    }

    @Override
    public DockerRepository getDockerRepositoryByIdentifier(String namespace, String repository) {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(repository);
        return repositoryDetailsRepository.getDockerRepositoryByIdentifier(namespace, repository);
    }

    @Override
    public DockerRepository save(DockerRepository repository) {
        // the return value of method save will never be null
        return repositoryDetailsRepository.save(repository);
    }
}
