package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.security.details.Action;
import cn.shaoqunliu.c.hub.mgr.service.DockerPermissionService;
import cn.shaoqunliu.c.hub.mgr.validation.ParameterValidationConstraints;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import cn.shaoqunliu.c.hub.utils.DockerImageIdentifier;
import cn.shaoqunliu.c.hub.utils.SecurityContextHolderUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/v1/permission")
public class MgrPermissionController {

    private final DockerPermissionService permissionService;

    @Autowired
    public MgrPermissionController(DockerPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @RequestMapping(value = "/{namespace}/{repository}", method = RequestMethod.PATCH, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RestfulResult repositoryChangeOwner(@PathVariable("namespace")
                                               @Pattern(regexp = ParameterValidationConstraints.namespace)
                                                       String namespace,
                                               @PathVariable("repository")
                                               @Pattern(regexp = ParameterValidationConstraints.repository)
                                                       String repository,
                                               @RequestBody @NotNull @Valid
                                                       JSONObject jsonObject) throws ResourceNotFoundException {
        DockerImageIdentifier identifier = new DockerImageIdentifier(namespace, repository);
        if (!jsonObject.containsKey("new_owner")) {
            // bad request body
            throw new IllegalArgumentException("no field named new_owner in request body json");
        }
        String newOwner = jsonObject.getString("new_owner");
        permissionService.changeOwnerOfRepository(identifier,
                SecurityContextHolderUtils.getUid(), newOwner);
        return new RestfulResult(HttpStatus.ACCEPTED.value(), "owner changed");
    }

    @RequestMapping(value = "/{namespace}/{repository}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RestfulResult changeDockerPermission(@PathVariable("namespace")
                                                @Pattern(regexp = ParameterValidationConstraints.namespace)
                                                        String namespace,
                                                @PathVariable("repository")
                                                @Pattern(regexp = ParameterValidationConstraints.repository)
                                                        String repository,
                                                @RequestBody @NotNull @Valid
                                                        JSONObject jsonObject) throws ResourceNotFoundException {
        if (!jsonObject.containsKey("username") ||
                !jsonObject.containsKey("action")) {
            throw new IllegalArgumentException("incomplete request body parameters");
        }
        String username = jsonObject.getString("username");
        Action action = Action.of(jsonObject.getString("action"));
        if (action== Action.NULL || action ==Action.PUSH) {
            throw new IllegalArgumentException("Bad action");
        }
        DockerImageIdentifier identifier = new DockerImageIdentifier(namespace, repository);
        permissionService.changeDockerPermissions(identifier, username, action);
        return new RestfulResult(HttpStatus.ACCEPTED.value(), "permission granted");
    }
}
