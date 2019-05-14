package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.jpa.DockerImageRepository;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerRepositoryDetailsRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasic;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasicWithoutOwner;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBriefDescription;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryDescription;
import cn.shaoqunliu.c.hub.mgr.service.DockerRepositoryService;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import cn.shaoqunliu.c.hub.utils.ObjectCopyingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service("myDockerRepositoryService")
public class MyDockerRepositoryService implements DockerRepositoryService {

    private final DockerRepositoryDetailsRepository repositoryDetailsRepository;
    private final DockerImageRepository imageRepository;

    @Autowired
    public MyDockerRepositoryService(DockerRepositoryDetailsRepository repositoryDetailsRepository, DockerImageRepository imageRepository) {
        this.repositoryDetailsRepository = repositoryDetailsRepository;
        this.imageRepository = imageRepository;
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
    public Page<DockerRepositoryBriefDescription> getBriefDescriptionByNamespace(String namespace, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return repositoryDetailsRepository.findAllByNamespaceNameOrderByStarsDesc(
                Objects.requireNonNull(namespace), pageRequest);
    }

    @Override
    public Page<DockerRepositoryBriefDescription> getPublicRepositoryBriefDescription(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return repositoryDetailsRepository.findAllByOpenedIsTrueOrderByStarsDesc(pageRequest);
    }

    @Override
    public Page<DockerRepositoryBriefDescription> getBriefDescriptionByFuzzyName(String name, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return repositoryDetailsRepository.findAllByNameContainsOrderByStarsDesc(
                Objects.requireNonNull(name), pageRequest
        );
    }

    @Override
    public Page<DockerRepositoryBriefDescription> getBriefDescriptionByOwner(int uid, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        DockerUser owner = new DockerUser();
        owner.setId(uid);
        return repositoryDetailsRepository.findAllByOwnerOrderByStarsDesc(owner, pageRequest);
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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteByIdentifier(DockerImageIdentifier identifier) {
        Objects.requireNonNull(identifier);
        // get repository deleted
        DockerRepositoryBasic repositoryBasic =
                repositoryDetailsRepository.getDockerRepositoryBasicByNamespaceNameAndName(
                        Objects.requireNonNull(identifier.getNamespace()),
                        Objects.requireNonNull(identifier.getRepository())
                );
        if (repositoryBasic == null) {
            return;
        }
        // deleted images within this repository
        DockerRepository repository = new DockerRepository();
        repository.setId(repositoryBasic.getId());
        imageRepository.deleteByRepository(repository);
        // delete repository info
        repositoryDetailsRepository.deleteById(repositoryBasic.getId());
    }
}
