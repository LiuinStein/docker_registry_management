package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public RestfulResult constraintViolationException(ConstraintViolationException e) {
        return new RestfulResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceNeedCreatedAlreadyExistsException.class)
    public RestfulResult resourceNeedCreatedAlreadyExistsException(ResourceNeedCreatedAlreadyExistsException e) {
        return new RestfulResult(HttpStatus.CONFLICT.value(), e.getMessage());
    }
}
