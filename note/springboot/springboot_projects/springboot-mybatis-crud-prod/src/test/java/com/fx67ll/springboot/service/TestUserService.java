package com.fx67ll.springboot.service;

import com.fx67ll.springboot.Starter;
import com.fx67ll.springboot.po.User;
import com.fx67ll.springboot.query.UserQuery;
import com.fx67ll.springboot.srevice.UserService;
import com.github.pagehelper.PageInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Service业务方法测试
 *
 * Junit中的RunWith注解 表示该类是单元测试的执行类
 * SpringRunner 是 spring-test 提供的测试执行单元类（是Spring单元测试中SpringJUnit4ClassRunner的新名字）
 * SpringBootTest注解 是执行测试程序的引导类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class})
public class TestUserService {

    // 日志的使用
    private Logger logger = LoggerFactory.getLogger(TestUserService.class);

    @Resource
    private UserService userService;

    @Before
    public void before() {
        logger.info("单元测试开始......");
    }

    @Test
    public void testQueryUserById() {
        logger.info("测试根据用户id查询......");

        User user = userService.queryUserById(1);
        logger.info("用户记录: ", user.toString());
    }

    @Test
    public void testSelectUserListByParams() {
        logger.info("测试根据分页条件查询用户列表......");

        UserQuery userQuery = new UserQuery();
        PageInfo<User> pageInfo = userService.selectUserListByParams(userQuery);
        logger.info("用户列表: ", pageInfo.toString());
    }

    @After
    public void after() {
        logger.info("单元测试结束......");
    }
}