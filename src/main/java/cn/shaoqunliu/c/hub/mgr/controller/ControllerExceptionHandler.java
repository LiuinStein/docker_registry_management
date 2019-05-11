package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
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

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public RestfulResult resourceNotFoundException(ResourceNotFoundException e) {
        return new RestfulResult(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {
            // for those java.util.Objects.requireNonNull
            NullPointerException.class
    })
    public RestfulResult serverError(Exception e) {
        return new RestfulResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage());
    }
}
