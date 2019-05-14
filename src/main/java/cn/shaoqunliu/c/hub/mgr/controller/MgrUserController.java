package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.po.MgrUser;
import cn.shaoqunliu.c.hub.mgr.service.MgrUserService;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/v1/user")
public class MgrUserController {

    private final MgrUserService userService;

    @Autowired
    public MgrUserController(MgrUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public RestfulResult signUp(@RequestBody @Valid
                                @NotNull MgrUser user) throws ResourceNeedCreatedAlreadyExistsException {
        Integer id = userService.signUp(user);
        if (id == null) {
            throw new ResourceNeedCreatedAlreadyExistsException("username or email already exists");
        }
        RestfulResult result = new RestfulResult(HttpStatus.CREATED.value(),
                "user created");
        result.addData("id", id);
        return result;
    }
}
