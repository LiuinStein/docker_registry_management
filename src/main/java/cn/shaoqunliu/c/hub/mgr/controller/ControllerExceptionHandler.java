package cn.shaoqunliu.c.hub.mgr.controller;

import cn.shaoqunliu.c.hub.mgr.exception.PageNumberOutOfRangeException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNeedCreatedAlreadyExistsException;
import cn.shaoqunliu.c.hub.mgr.exception.ResourceNotFoundException;
import cn.shaoqunliu.c.hub.mgr.vo.RestfulResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {
            ConstraintViolationException.class,
            // for constraint violation caused in a Java POJO
            // with an @Valid annotation as a request parameter in controller
            MethodArgumentNotValidException.class,
            // throw at when spring mvc controller try to convert a bad boolean
            // value within URL parameters.
            IllegalArgumentException.class,
            // occurred when an empty Http request body was sent to a controller
            // method which need a described body info
            HttpMessageNotReadableException.class,
            PageNumberOutOfRangeException.class
    })
    public RestfulResult constraintViolationException(Exception e) {
        return new RestfulResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {
            BadCredentialsException.class
    })
    public RestfulResult authenticationError(Exception e) {
        return new RestfulResult(HttpStatus.FORBIDDEN.value(), e.getMessage());
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
