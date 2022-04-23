package com.fx67ll.springboot.srevice;

import com.fx67ll.springboot.dao.UserMapper;
import com.fx67ll.springboot.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User queryUserByUsername(String userName) {
        return userMapper.queryUserByUsername(userName);
    }
}
