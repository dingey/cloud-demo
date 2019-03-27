package com.d.user.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "主页")
@RestController
public class IndexController {
    @Value("${spring.application.name}")
    private String appName;

    @ApiOperation("主页")
    @GetMapping(path = "/")
    public String hi() {
        return appName;
    }
}
