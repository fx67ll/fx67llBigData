package com.fx67ll.springboot.dao;

import com.fx67ll.springboot.po.User;
import com.fx67ll.springboot.query.UserQuery;

import java.util.List;

/**
 * sql语句dao层
 */
public interface UserMapper {

    // 通过用户名查询用户对象
    User queryUserByUsername(String userName);

    // 通过用户ID查询用户对象
    User queryUserById(Integer id);

    // 添加用户，返回受影响行数，所以返回类型是int
    int saveUser(User user);

    // 修改用户
    int updateUser(User user);

    // 删除用户
    int deleteUserById(Integer id);

    // 根据条件，分页查询用户列表
    List<User> selectUserListByParams(UserQuery userQuery);
}
