package com.fx67ll.springboot.controller;

import com.fx67ll.springboot.pojo.User;
import com.fx67ll.springboot.srevice.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/user/{userName}")
    public User queryUserByUsername(@PathVariable String userName) {
        return userService.queryUserByUsername(userName);
    }
}
