package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerImageRepository;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerNamespaceRepository;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerRepositoryDetailsRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerNamespaceWithoutOwner;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasicWithoutOwner;
import cn.shaoqunliu.c.hub.mgr.service.DockerNamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service("myDockerNamespaceService")
public class MyDockerNamespaceService implements DockerNamespaceService {

    private final DockerNamespaceRepository namespaceRepository;
    private final DockerRepositoryDetailsRepository repositoryDetailsRepository;
    private final DockerImageRepository imageRepository;

    @Autowired
    public MyDockerNamespaceService(DockerNamespaceRepository namespaceRepository, DockerRepositoryDetailsRepository repositoryDetailsRepository, DockerImageRepository imageRepository) {
        this.namespaceRepository = namespaceRepository;
        this.repositoryDetailsRepository = repositoryDetailsRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public DockerNamespace getDockerNamespaceByName(String name) {
        Objects.requireNonNull(name);
        return namespaceRepository.getDockerNamespaceByName(name);
    }

    @Override
    public Page<DockerNamespaceWithoutOwner> getNamespacesByOwner(int uid, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        DockerUser user = new DockerUser();
        user.setId(uid);
        return namespaceRepository.findAllByOwner(user, pageRequest);
    }

    @Override
    public DockerNamespace addNamespaceIfNotExists(String namespace, int uid) throws ResourceNeedCreatedAlreadyExistsException {
        if (namespaceRepository.existsByName(namespace)) {
            throw new ResourceNeedCreatedAlreadyExistsException(
                    "the namespace " + namespace + " already exists"
            );
        }
        DockerNamespace dockerNamespace = new DockerNamespace();
        dockerNamespace.setName(namespace);
        DockerUser owner = new DockerUser();
        owner.setId(uid);
        dockerNamespace.setOwner(owner);
        return namespaceRepository.save(dockerNamespace);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteByName(String namespace) {
        // find all repositories within the requested namespace
        List<DockerRepositoryBasicWithoutOwner> repositories =
                repositoryDetailsRepository.findAllByNamespaceName(
                        Objects.requireNonNull(namespace)
                );
        if (repositories != null && repositories.size() != 0) {
            // delete all repositories
            for (DockerRepositoryBasicWithoutOwner repository : repositories) {
                // delete images within this repository
                DockerRepository deleted = new DockerRepository();
                deleted.setId(repository.getId());
                imageRepository.deleteByRepository(deleted);
                // delete repository
                repositoryDetailsRepository.deleteById(repository.getId());
            }
            // delete namespace without get id again
            namespaceRepository.deleteById(
                    repositories.get(0).getNamespace().getId()
            );
            return;
        }
        // delete this namespace
        namespaceRepository.deleteByName(namespace);
    }
}
