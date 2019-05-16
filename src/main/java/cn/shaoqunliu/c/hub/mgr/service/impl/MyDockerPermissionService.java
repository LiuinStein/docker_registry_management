package cn.shaoqunliu.c.hub.mgr.service.impl;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerPermissionRepository;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerRepositoryDetailsRepository;
import cn.shaoqunliu.c.hub.mgr.jpa.DockerUserRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerPermission;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.DockerUser;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerPermissionBasic;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBasic;
import cn.shaoqunliu.c.hub.mgr.security.details.Action;
import cn.shaoqunliu.c.hub.mgr.service.DockerPermissionService;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service("myDockerPermissionService")
public class MyDockerPermissionService implements DockerPermissionService {

    private final DockerUserRepository userRepository;
    private final DockerRepositoryDetailsRepository repositoryDetailsRepository;
    private final DockerPermissionRepository permissionRepository;

    @Autowired
    public MyDockerPermissionService(DockerUserRepository userRepository, DockerRepositoryDetailsRepository repositoryDetailsRepository, DockerPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.repositoryDetailsRepository = repositoryDetailsRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public void changeOwnerOfRepository(DockerImageIdentifier identifier,
                                        int namespaceOwner, String newOwner) throws ResourceNotFoundException {
        Objects.requireNonNull(identifier);
        DockerUser user = userRepository.getDockerUserByUsername(
                Objects.requireNonNull(newOwner));
        if (user == null) {
            throw new ResourceNotFoundException("the new owner with username " + newOwner + " was not found");
        }
        DockerRepository repository = repositoryDetailsRepository
                .getDockerRepositoryByNamespaceNameAndName(
                        Objects.requireNonNull(identifier.getNamespace()),
                        Objects.requireNonNull(identifier.getRepository())
                );
        if (repository == null) {
            throw new ResourceNotFoundException("the repository named " + identifier.getFullRepositoryName() + " was not found");
        }
        if (repository.getNamespace().getId() != namespaceOwner ||
                // check if it is the change of owner of this repository twice
                !repository.getOwner().getId().equals(repository.getNamespace().getId())) {
            throw new BadCredentialsException("the operator is not the owner of this namespace or the repository can not change owner twice");
        }
        repositoryDetailsRepository.changeOwner(user, repository.getId());
    }

    @Override
    public void changeDockerPermissions(DockerImageIdentifier identifier, String username, Action action) throws ResourceNotFoundException {
        Objects.requireNonNull(identifier);
        DockerRepositoryBasic repositoryBasic = repositoryDetailsRepository.getDockerRepositoryBasicByNamespaceNameAndName(
                Objects.requireNonNull(identifier.getNamespace()),
                Objects.requireNonNull(identifier.getRepository())
        );
        if (repositoryBasic == null) {
            throw new ResourceNotFoundException("the required repository with identifier " + identifier.getFullRepositoryName() + " was not found");
        }
        DockerUser user = userRepository.getDockerUserByUsername(
                Objects.requireNonNull(username)
        );
        if (user == null) {
            throw new ResourceNotFoundException("the user with username " + username + " is not exists");
        }
        DockerRepository repository = new DockerRepository();
        repository.setId(repositoryBasic.getId());
        DockerPermission permission = new DockerPermission();
        permission.setUser(user);
        permission.setRepository(repository);
        permission.setAction(Objects.requireNonNull(action).value());
        permissionRepository.save(permission);
    }

    @Override
    public Page<DockerPermissionBasic> getPermissionsByRepository(DockerImageIdentifier identifier, int page) {
        Objects.requireNonNull(identifier);
        PageRequest pageRequest = PageRequest.of(page, 10);
        return permissionRepository.findAllByRepositoryNamespaceNameAndRepositoryName(
                Objects.requireNonNull(identifier.getNamespace()),
                Objects.requireNonNull(identifier.getRepository()),
                pageRequest
        );
    }
}
