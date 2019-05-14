package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.service.DockerStarsService;
import cn.shaoqunliu.c.hub.mgr.validation.ParameterValidationConstraints;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import cn.shaoqunliu.c.hub.utils.SecurityContextHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/v1/star")
public class MgrStarController {

    private final DockerStarsService starsService;

    @Autowired
    public MgrStarController(DockerStarsService starsService) {
        this.starsService = starsService;
    }

    @RequestMapping(value = "/{namespace}/{repository}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void starRepository(@PathVariable("namespace")
                               @Pattern(regexp = ParameterValidationConstraints.namespace)
                                       String namespace,
                               @PathVariable("repository")
                               @Pattern(regexp = ParameterValidationConstraints.repository)
                                       String repository) {
        starsService.starRepository(new DockerImageIdentifier(namespace, repository),
                SecurityContextHolderUtils.getUid());
    }

    @RequestMapping(value = "/{namespace}/{repository}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unStarRepository(@PathVariable("namespace")
                                 @Pattern(regexp = ParameterValidationConstraints.namespace)
                                         String namespace,
                                 @PathVariable("repository")
                                 @Pattern(regexp = ParameterValidationConstraints.repository)
                                         String repository) {
        starsService.unStarRepository(new DockerImageIdentifier(namespace, repository),
                SecurityContextHolderUtils.getUid());
    }

    @RequestMapping(value = "/{namespace}/{repository}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void ifStarred(@PathVariable("namespace")
                          @Pattern(regexp = ParameterValidationConstraints.namespace)
                                  String namespace,
                          @PathVariable("repository")
                          @Pattern(regexp = ParameterValidationConstraints.repository)
                                  String repository) throws ResourceNotFoundException {
        if (!starsService.ifStarred(new DockerImageIdentifier(namespace, repository),
                SecurityContextHolderUtils.getUid())) {
            throw new ResourceNotFoundException("un-starred");
        }
    }
}
