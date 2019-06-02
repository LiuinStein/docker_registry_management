package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.po.MgrUser;
import cn.shaoqunliu.c.hub.mgr.po.MgrUserInfo;
import cn.shaoqunliu.c.hub.mgr.service.MgrUserService;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import cn.shaoqunliu.c.hub.utils.SecurityContextHolderUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Objects;

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

    @RequestMapping(value = "/password", method = RequestMethod.PATCH, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RestfulResult changePassword(@RequestBody JSONObject jsonObject) {
        String raw, newer;
        Integer id;
        if (jsonObject.containsKey("raw_c") && jsonObject.containsKey("new_c")) {
            // change docker client password
            raw = Objects.requireNonNull(jsonObject.getString("raw_c"));
            newer = Objects.requireNonNull(jsonObject.getString("new_c"));
            id = userService.changeDockerClientPassword(
                    SecurityContextHolderUtils.getUid(), raw, newer
            );
        } else if (jsonObject.containsKey("raw_m") && jsonObject.containsKey("new_m")) {
            raw = Objects.requireNonNull(jsonObject.getString("raw_m"));
            newer = Objects.requireNonNull(jsonObject.getString("new_m"));
            id = userService.changeMasterPassword(
                    SecurityContextHolderUtils.getUid(), raw, newer
            );
        } else {
            throw new IllegalArgumentException("the parameters within changing password request is illegal");
        }
        if (id == null || newer.length() < 6 || newer.length() > 32) {
            throw new BadCredentialsException("raw password error");
        }
        return new RestfulResult(HttpStatus.ACCEPTED.value(),
                "The required password change has been accomplished");
    }

    @RequestMapping(value = "/info", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RestfulResult updateUserInfo(@RequestBody @Valid MgrUserInfo userInfo) {
        userInfo.setId(SecurityContextHolderUtils.getUid());
        if (userService.updateUserInfo(userInfo) == null) {
            throw new BadCredentialsException("bad credentials");
        }
        return new RestfulResult(HttpStatus.ACCEPTED.value(), "user info updated");
    }

    @RequestMapping(value = {"/info", "/info/{username}"}, method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public RestfulResult getUserInfo(@PathVariable(value = "username", required = false)
                                             String username) throws ResourceNotFoundException {
        if (username == null || username.length() == 0) {
            username = SecurityContextHolderUtils.getUsername();
        }
        MgrUserInfo info = userService.getUserInfoByUsername(username);
        if (info == null) {
            throw new ResourceNotFoundException("the user with name " + username + " was not found");
        }
        RestfulResult result = new RestfulResult(HttpStatus.OK.value(), "user info got");
        result.addData("users", Collections.singletonList(info));
        return result;
    }
}
