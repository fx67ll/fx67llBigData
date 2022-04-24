package com.fx67ll.springboot.srevice;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fx67ll.springboot.dao.UserMapper;
import com.fx67ll.springboot.po.User;
import com.fx67ll.springboot.po.vo.ResultInfo;
import com.fx67ll.springboot.query.UserQuery;
import com.fx67ll.springboot.utils.AssertUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
     * <p>
     * 使用缓存将查询到User对象存储fx67llCache的缓存空间中，用userName作为键值
     *
     * @param userName
     * @return
     */
    @Cacheable(value = "fx67llCache", key = "#userName")
    public User queryUserByUsername(String userName) {
        return userMapper.queryUserByUsername(userName);
    }

    /**
     * 根据用户ID查询用户对象
     *
     * @param id
     * @return
     */
    @Cacheable(value = "fx67llCache", key = "#id")
    public User queryUserById(Integer id) {

        // 测试抛出异常
        // AssertUtils.isTrue(true,"fx67ll -> 异常测试......");

        return userMapper.queryUserById(id);
    }

    /**
     * 添加用户
     *
     * @param user
     */
    public void saveUser(User user) {
        // 验证用户名和用户密码是否为空，自定义工具类和异常
        // 后面因为使用了全局异常就不需要这样每个地方都定义错误参数拦截
        // AssertUtils.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空！");
        // AssertUtils.isTrue(StringUtils.isBlank(user.getUserPwd()), "用户密码不能为空！");

        // 通过用户名查询用户对象是否存在
        User tempUser = userMapper.queryUserByUsername(user.getUserName());
        AssertUtils.isTrue(null != tempUser, "该用户已存在！");

        // sql执行的影响行数小于1说明添加失败了
        AssertUtils.isTrue(userMapper.saveUser(user) < 1, "添加用户失败！");
    }

    /**
     * 修改用户
     * <p>
     * 如果数据库改了而缓存没改就会导致错误，所以会有针对更新操作的缓存操作注解，删除同理
     * 修改过后，缓存也变了，重新查询会继续从缓存里面读取
     *
     * @param user
     */
    // 声明式事务注解
    // @Transactional(propagation = Propagation.REQUIRED)
    @CachePut(value = "fx67llCache", key = "#user.id")
    public User updateUser(User user) {
        // 验证用户名和用户密码是否为空，自定义工具类和异常
        AssertUtils.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空！");
        AssertUtils.isTrue(StringUtils.isBlank(user.getUserPwd()), "用户密码不能为空！");

        // 通过用户名查询用户对象是否存在，不能修改为已经存在的对象
        User tempUser = userMapper.queryUserByUsername(user.getUserName());
        AssertUtils.isTrue(null != tempUser && !(tempUser.getId().equals(user.getId())), "该用户名已存在，不能修改！");

        AssertUtils.isTrue(userMapper.updateUser(user) < 1, "修改用户失败！");

        // 测试声明式事务注解，TODO 出现异常
        // int i = 1/0;

        // 配合缓存，添加一个user作为返回值
        return user;
    }


    /**
     * 删除用户
     *
     * @param id
     */
    @CacheEvict(value = "fx67llCache", key = "#id")
    public Integer deleteUserById(Integer id) {
        AssertUtils.isTrue(null == id && null == userMapper.queryUserById(id), "待删除的用户记录不存在！");
        AssertUtils.isTrue(userMapper.deleteUserById(id) < 1, "删除用户失败！");
        return id;
    }

    /**
     * 根据条件，分页查询用户列表
     * PageInfo是分页插件中返回列表的对象
     * <p>
     * 缓存多个参数可以用拼接的方式来完成
     *
     * @param userQuery
     * @return
     */
    @Cacheable(value = "fx67llCache", key = "#userQuery.userName + '-' + #userQuery.pageNum + '-' + #userQuery.pageNum")
    public PageInfo<User> selectUserListByParams(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPageNum(), userQuery.getPageSize());
        return new PageInfo<User>(userMapper.selectUserListByParams(userQuery));
    }
}
