package com.fx67ll.springboot.srevice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fx67ll.springboot.dao.UserMapper;
import com.fx67ll.springboot.po.User;
import com.fx67ll.springboot.query.UserQuery;
import com.fx67ll.springboot.utils.AssertUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 业务逻辑service层
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户对象
     * @param userName
     * @return
     */
    public User queryUserByUsername(String userName) {
        return userMapper.queryUserByUsername(userName);
    }

    /**
     * 根据用户ID查询用户对象
     * @param id
     * @return
     */
    public User queryUserById(Integer id) {
        return userMapper.queryUserById(id);
    }

    /**
     * 添加用户
     * @param user
     */
    public void saveUser(User user) {
        // 验证用户名和用户密码是否为空，自定义工具类和异常
        AssertUtils.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空！");
        AssertUtils.isTrue(StringUtils.isBlank(user.getUserPwd()), "用户密码不能为空！");

        // 通过用户名查询用户对象是否存在
        User tempUser = userMapper.queryUserByUsername(user.getUserName());
        AssertUtils.isTrue(null != tempUser, "该用户已存在！");

        // sql执行的影响行数小于1说明添加失败了
        AssertUtils.isTrue(userMapper.saveUser(user) < 1, "添加用户失败！");
    }

    /**
     * 修改用户
     * @param user
     */
    public void updateUser(User user) {
        // 验证用户名和用户密码是否为空，自定义工具类和异常
        AssertUtils.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空！");
        AssertUtils.isTrue(StringUtils.isBlank(user.getUserPwd()), "用户密码不能为空！");

        // 通过用户名查询用户对象是否存在，不能修改为已经存在的对象
        User tempUser = userMapper.queryUserByUsername(user.getUserName());
        AssertUtils.isTrue(null != tempUser && !(tempUser.getId().equals(user.getId())), "该用户名已存在，不能修改！");

        AssertUtils.isTrue(userMapper.updateUser(user) < 1, "修改用户失败！");
    }


    /**
     * 删除用户
     * @param id
     */
    public void deleteUserById(Integer id) {
        AssertUtils.isTrue(null == id && null == userMapper.queryUserById(id), "待删除的用户记录不存在！");
        AssertUtils.isTrue(userMapper.deleteUserById(id) < 1, "删除用户失败！");
    }

    /**
     * 根据条件，分页查询用户列表
     * PageInfo是分页插件中返回列表的对象
     * @param userQuery
     * @return
     */
    public PageInfo<User> selectUserListByParams(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPageNum(), userQuery.getPageSize());
        return new PageInfo<User>(userMapper.selectUserListByParams(userQuery));
    }
}
