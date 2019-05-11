package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.PageNumberOutOfRangeException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.po.DockerNamespace;
import cn.shaoqunliu.c.hub.mgr.po.projection.DockerNamespaceWithoutOwner;
import cn.shaoqunliu.c.hub.mgr.service.DockerNamespaceService;
import cn.shaoqunliu.c.hub.mgr.validation.ParameterValidationConstraints;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import cn.shaoqunliu.c.hub.utils.SecurityContextHolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.Collections;

@Validated
@RestController
@RequestMapping("/v1/namespace")
public class MgrNamespaceController {

    private final DockerNamespaceService namespaceService;

    @Autowired
    public MgrNamespaceController(DockerNamespaceService namespaceService) {
        this.namespaceService = namespaceService;
    }

    @RequestMapping(value = "/{namespace}", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public RestfulResult addNamespace(@PathVariable("namespace")
                                      @Pattern(regexp = ParameterValidationConstraints.namespace) String namespace) throws ResourceNeedCreatedAlreadyExistsException {
        // the @PathVariable will assure the namespace value within URL is not null
        Integer uid = SecurityContextHolderUtils.getUid();
        RestfulResult result = new RestfulResult(HttpStatus.CREATED.value(),
                "namespace created");
        result.addData("nid", namespaceService.addNamespaceIfNotExists(namespace, uid).getId());
        return result;
    }

    @RequestMapping(value = "/{namespace}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public RestfulResult getOneSpecificNamespace(@PathVariable("namespace")
                                                 @Pattern(regexp = ParameterValidationConstraints.namespace) String namespace) throws ResourceNotFoundException {
        DockerNamespace dockerNamespace = namespaceService.getDockerNamespaceByName(namespace);
        if (dockerNamespace == null) {
            throw new ResourceNotFoundException("no namespace was found which named " + namespace);
        }
        RestfulResult result = new RestfulResult(HttpStatus.OK.value(),
                "required resource founded and shown in follow data");
        result.addData("namespaces", Collections.singletonList(dockerNamespace));
        return result;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public RestfulResult getNamespacesOfSomeone(@RequestParam(value = "page", required = false) Integer page) throws ResourceNotFoundException, PageNumberOutOfRangeException {
        Integer uid = SecurityContextHolderUtils.getUid();
        Page<DockerNamespaceWithoutOwner> namespaces =
                namespaceService.getNamespacesByOwner(uid,
                        // page starts from 0 instead of 1
                        page == null ? 0 : page);
        if (namespaces.getTotalElements() == 0) {
            throw new ResourceNotFoundException("no such namespaces was found which belongs to user with uid " + uid);
        }
        if (namespaces.getNumberOfElements() == 0) {
            throw new PageNumberOutOfRangeException("page number " + page + " out of range");
        }
        RestfulResult result = new RestfulResult(HttpStatus.OK.value(),
                "required resource founded and shown in follow data");
        result.addData("namespaces", namespaces.getContent());
        result.addData("total", namespaces.getTotalElements());
        return result;
    }
}
