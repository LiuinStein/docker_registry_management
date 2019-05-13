package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.PageNumberOutOfRangeException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.po.DockerRepository;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryBriefDescription;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerRepositoryDescription;
import cn.shaoqunliu.c.hub.mgr.service.DockerRepositoryService;
import cn.shaoqunliu.c.hub.mgr.validation.ParameterValidationConstraints;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

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

    @RequestMapping(value = "/{namespace}/{repository}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public RestfulResult getOneSpecificRepository(@PathVariable("namespace")
                                                  @Pattern(regexp = ParameterValidationConstraints.namespace)
                                                          String namespace,
                                                  @PathVariable("repository")
                                                  @Pattern(regexp = ParameterValidationConstraints.repository)
                                                          String repository) throws ResourceNotFoundException {
        DockerRepositoryDescription description = repositoryService
                .getDockerRepositoryDescriptionByIdentifier(namespace, repository);
        if (description == null) {
            throw new ResourceNotFoundException("the required repository with identifier " + namespace + "/" + repository + " was not found");
        }
        RestfulResult result = new RestfulResult(HttpStatus.OK.value(), "repository found");
        result.addData("repositories", Collections.singletonList(description));
        return result;
    }

    @RequestMapping(value = "/{namespace}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public RestfulResult getRepositoriesByNamespace(@PathVariable("namespace")
                                                    @Pattern(regexp = ParameterValidationConstraints.namespace)
                                                            String namespace,
                                                    @RequestParam(value = "page", required = false)
                                                    @PositiveOrZero Integer page) throws ResourceNotFoundException, PageNumberOutOfRangeException {
        Page<DockerRepositoryBriefDescription> descriptions =
                repositoryService.getBriefDescriptionByNamespace(namespace,
                        page == null ? 0 : page);
        if (descriptions.getTotalElements() == 0) {
            throw new ResourceNotFoundException("no repositories was found with namespace " + namespace);
        }
        if (descriptions.getNumberOfElements() == 0) {
            throw new PageNumberOutOfRangeException("page number " + page + " out of range");
        }
        RestfulResult result = new RestfulResult(HttpStatus.OK.value(), "required repositories founded");
        result.addData("repositories", descriptions.getContent());
        result.addData("total", descriptions.getTotalElements());
        return result;
    }

}
