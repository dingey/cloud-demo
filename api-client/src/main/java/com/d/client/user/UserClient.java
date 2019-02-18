package com.d.client.user;

import com.d.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "USER-SERVICE")
public interface UserClient {
    @RequestMapping(path = "/user/{id}", method = RequestMethod.GET)
    User get(@PathVariable("id") Long id);
}
