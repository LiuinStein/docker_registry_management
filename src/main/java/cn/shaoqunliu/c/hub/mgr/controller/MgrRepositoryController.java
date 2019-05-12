package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.service.DockerRepositoryService;
import cn.shaoqunliu.c.hub.mgr.validation.ParameterValidationConstraints;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/v1/repository")
public class MgrRepositoryController {

    private final DockerRepositoryService repositoryService;

    @Autowired
    public MgrRepositoryController(DockerRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @RequestMapping(value = "/{namespace}/{repository}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RestfulResult updateInfo(@PathVariable("namespace")
                                    @Pattern(regexp = ParameterValidationConstraints.namespace)
                                            String namespace,
                                    @PathVariable("repository")
                                    @Pattern(regexp = ParameterValidationConstraints.repository)
                                            String repository,
                                    @RequestBody @Valid
                                            DockerRepository details) throws ResourceNotFoundException {
        DockerImageIdentifier identifier = new DockerImageIdentifier(namespace, repository);
        if (repositoryService.update(identifier, details) == null) {
            throw new ResourceNotFoundException("the requested repository with identifier " + identifier.getFullRepositoryName() + " is not found in this server");
        }
        return new RestfulResult(HttpStatus.ACCEPTED.value(), "update success");
    }
}
