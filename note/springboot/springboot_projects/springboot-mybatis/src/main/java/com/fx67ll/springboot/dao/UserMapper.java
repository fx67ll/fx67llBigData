package com.fx67ll.springboot.dao;

import com.fx67ll.springboot.pojo.User;

public interface UserMapper {

    // 通过用户名查询用户对象
    User queryUserByUsername(String userName);

}
