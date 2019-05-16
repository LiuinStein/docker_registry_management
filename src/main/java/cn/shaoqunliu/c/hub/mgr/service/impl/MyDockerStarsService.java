package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.exception.PageNumberOutOfRangeException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerRepositoryDetailsRepository;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerStarRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerStars;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBriefDescription;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryStars;
import cn.shaoqunliu.c.hub.mgr.service.DockerStarsService;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("myDockerStarsService")
public class MyDockerStarsService implements DockerStarsService {

    private final DockerStarRepository starRepository;
    private final DockerRepositoryDetailsRepository repositoryDetailsRepository;

    @Autowired
    public MyDockerStarsService(DockerStarRepository starRepository, DockerRepositoryDetailsRepository repositoryDetailsRepository) {
        this.starRepository = starRepository;
        this.repositoryDetailsRepository = repositoryDetailsRepository;
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void starRepository(DockerImageIdentifier identifier, int uid) {
        Objects.requireNonNull(identifier);
        DockerRepositoryStars repositoryStars =
                repositoryDetailsRepository.getDockerRepositoryStarsByNamespaceNameAndName(
                        Objects.requireNonNull(identifier.getNamespace()),
                        Objects.requireNonNull(identifier.getRepository())
                );
        // check if already starred
        if (repositoryStars == null ||
                starRepository.existsByPkUidAndPkRid(uid,
                        Objects.requireNonNull(repositoryStars.getId()))) {
            // not star again
            return;
        }
        // record in docker star table
        DockerStars stars = new DockerStars();
        stars.setPk(uid, repositoryStars.getId()); // getId() already not null
        starRepository.save(stars);
        // +1 in repository details table
        repositoryDetailsRepository.starOnce(1L, repositoryStars.getId());
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void unStarRepository(DockerImageIdentifier identifier, int uid) {
        Objects.requireNonNull(identifier);
        DockerRepositoryStars repositoryStars =
                repositoryDetailsRepository.getDockerRepositoryStarsByNamespaceNameAndName(
                        Objects.requireNonNull(identifier.getNamespace()),
                        Objects.requireNonNull(identifier.getRepository())
                );
        // check if not starred
        if (repositoryStars == null ||
                !starRepository.existsByPkUidAndPkRid(uid,
                        Objects.requireNonNull(repositoryStars.getId()))) {
            // currently not starred
            return;
        }
        // un-star in starRepository
        starRepository.deleteById(new DockerStars.Pk(uid, repositoryStars.getId()));
        // -1 in repository details table
        repositoryDetailsRepository.starOnce(-1L, repositoryStars.getId());
    }

    @Override
    public boolean ifStarred(DockerImageIdentifier identifier, int uid) {
        Objects.requireNonNull(identifier);
        DockerRepositoryStars repositoryStars =
                repositoryDetailsRepository.getDockerRepositoryStarsByNamespaceNameAndName(
                        Objects.requireNonNull(identifier.getNamespace()),
                        Objects.requireNonNull(identifier.getRepository())
                );
        return repositoryStars != null &&
                starRepository.existsByPkUidAndPkRid(uid,
                        Objects.requireNonNull(repositoryStars.getId()));
    }

    @Override
    public List<DockerRepositoryBriefDescription> getStarredRepositoriesByUserId(int uid) throws ResourceNotFoundException {
        List<DockerStars> starred = starRepository.findAllByPkUid(uid);
        if (starred.size() == 0) {
            throw new ResourceNotFoundException("no starred repository was found");
        }
        List<DockerRepositoryBriefDescription> result = new ArrayList<>();
        starred.forEach(x -> {
            DockerRepositoryBriefDescription description =
                    repositoryDetailsRepository.getDockerRepositoryBriefDescriptionById(x.getPk().getRid());
            if (description == null) {
                return;
            }
            result.add(description);
        });
        return result;
    }
}
