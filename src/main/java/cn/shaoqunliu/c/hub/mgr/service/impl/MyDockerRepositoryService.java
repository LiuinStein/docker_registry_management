package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.DockerRepositoryDetailsRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasic;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryDescription;
import cn.shaoqunliu.c.hub.mgr.service.DockerRepositoryService;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import cn.shaoqunliu.c.hub.utils.ObjectCopyingUtils;
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
    public DockerRepositoryBasic getDockerRepositoryBasicByIdentifier(String namespace, String repository) {
        return repositoryDetailsRepository.getDockerRepositoryBasicByNamespaceNameAndName(
                Objects.requireNonNull(namespace), Objects.requireNonNull(repository));
    }

    @Override
    public DockerRepositoryDescription getDockerRepositoryDescriptionByIdentifier(String namespace, String repository) {
        return repositoryDetailsRepository.getDockerRepositoryDescriptionByNamespaceNameAndName(
                Objects.requireNonNull(namespace), Objects.requireNonNull(repository)
        );
    }

    @Override
    public DockerRepository save(DockerRepository repository) {
        // the return value of method save will never be null
        return repositoryDetailsRepository.save(repository);
    }

    @Override
    public Integer update(DockerImageIdentifier identifier, DockerRepository newer) {
        DockerRepository current = repositoryDetailsRepository
                .getDockerRepositoryByNamespaceNameAndName(
                        identifier.getNamespace(), identifier.getRepository()
                );
        if (current == null) {
            return null;
        }
        // never update stars
        newer.setStars(null);
        return repositoryDetailsRepository.save(
                ObjectCopyingUtils.copyNullProperties(current, newer)
        ).getId();
    }
}
