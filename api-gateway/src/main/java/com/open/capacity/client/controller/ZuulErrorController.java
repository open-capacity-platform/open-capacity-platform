package com.open.capacity.client.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netflix.zuul.context.RequestContext;
import com.open.capacity.commons.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * zuul自定义异常格式
 * (限流处理异常)
*/

@Slf4j
@Controller
public class ZuulErrorController implements ErrorController {

    public static final String ERROR_PATH = "/error";

    /**
     * 错误最终会到这里来
     */
    @RequestMapping(ERROR_PATH)
    @ResponseBody
    public Object error() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        return Result.failed(throwable.getMessage()) ;
    }

    

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}