package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.PageNumberOutOfRangeException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerImageBasic;
import cn.shaoqunliu.c.hub.mgr.service.DockerImageService;
import cn.shaoqunliu.c.hub.mgr.validation.ParameterValidationConstraints;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.ParameterOutOfBoundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping("/v1/image")
public class MgrImageController {

    private final DockerImageService imageService;

    @Autowired
    public MgrImageController(DockerImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/{namespace}/{repository}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public RestfulResult getImagesWithinRepository(@PathVariable("namespace")
                                                   @Pattern(regexp = ParameterValidationConstraints.namespace)
                                                           String namespace,
                                                   @PathVariable("repository")
                                                   @Pattern(regexp = ParameterValidationConstraints.repository)
                                                           String repository,
                                                   @RequestParam(value = "page", required = false)
                                                   @PositiveOrZero Integer page) throws ResourceNotFoundException, PageNumberOutOfRangeException {
        DockerImageIdentifier identifier = new DockerImageIdentifier(namespace, repository);
        Page<DockerImageBasic> images = imageService.getImagesFromRepository(identifier, page == null ? 0 : page);
        if (images.getTotalElements() == 0) {
            throw new ResourceNotFoundException("no images within repository with identifier " + identifier.getFullRepositoryName());
        }
        if (images.getNumberOfElements() == 0) {
            throw new PageNumberOutOfRangeException("page number " + page + " out of range");
        }
        RestfulResult result = new RestfulResult(HttpStatus.OK.value(),
                "all images founded was shown in following data");
        result.addData("total", images.getTotalElements());
        result.addData("images", images.getContent());
        return result;
    }

    @RequestMapping(value = "/{namespace}/{repository}/{tag}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByIdentifier(@PathVariable("namespace")
                                   @Pattern(regexp = ParameterValidationConstraints.namespace)
                                           String namespace,
                                   @PathVariable("repository")
                                   @Pattern(regexp = ParameterValidationConstraints.repository)
                                           String repository,
                                   @PathVariable("tag")
                                   @Pattern(regexp = ParameterValidationConstraints.tag)
                                           String tag) {
        imageService.deleteByIdentifier(new DockerImageIdentifier(namespace, repository, tag));
    }
}
