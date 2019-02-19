package com.d.user.web;

import com.d.user.config.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestController {
    @Autowired
    UserConfig userConfig;
    @Value("${server.host}")
    private String host;

    @GetMapping(path = "/host")
    public String host() {
        return userConfig.getHost() +" = "+ host;
    }
}
