package com.d.user.web;

import com.d.user.entity.User;
import com.d.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/open/login")
    public User login(HttpServletResponse response) {
        response.addCookie(new Cookie("token", "test"));
        return userService.get(1L);
    }

    @GetMapping(path = "/user/{id}")
    public User get(@PathVariable("id") Long id) {
        return userService.get(id);
    }
}
