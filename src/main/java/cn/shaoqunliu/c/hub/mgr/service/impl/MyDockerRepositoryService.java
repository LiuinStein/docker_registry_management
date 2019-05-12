package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.DockerRepositoryDescriptionRepository;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerRepositoryDetailsRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepositoryDescription;
import cn.shaoqunliu.c.hub.mgr.service.DockerRepositoryService;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import cn.shaoqunliu.c.hub.utils.ObjectCopyingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("myDockerRepositoryService")
public class MyDockerRepositoryService implements DockerRepositoryService {

    private final DockerRepositoryDetailsRepository repositoryDetailsRepository;
    private final DockerRepositoryDescriptionRepository repositoryDescriptionRepository;

    @Autowired
    public MyDockerRepositoryService(DockerRepositoryDetailsRepository repositoryDetailsRepository, DockerRepositoryDescriptionRepository repositoryDescriptionRepository) {
        this.repositoryDetailsRepository = repositoryDetailsRepository;
        this.repositoryDescriptionRepository = repositoryDescriptionRepository;
    }

    @Override
    public DockerRepository getDockerRepositoryByIdentifier(String namespace, String repository) {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(repository);
        return repositoryDetailsRepository.getDockerRepositoryByIdentifier(namespace, repository);
    }

    @Override
    public Boolean isOpened(String namespace, String repository) {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(repository);
        return repositoryDetailsRepository.getOpenedByIdentifier(namespace, repository);
    }

    @Override
    public DockerRepository save(DockerRepository repository) {
        // the return value of method save will never be null
        return repositoryDetailsRepository.save(repository);
    }

    @Override
    public Integer update(DockerImageIdentifier identifier, DockerRepositoryDescription newer) {
        DockerRepositoryDescription current = repositoryDescriptionRepository
                .getDockerRepositoryDescriptionByIdentifier(
                        identifier.getNamespace(), identifier.getRepository()
                );
        if (current == null) {
            return null;
        }
        return repositoryDescriptionRepository.save(
                ObjectCopyingUtils.copyNullProperties(current, newer)
        ).getId();
    }
}
