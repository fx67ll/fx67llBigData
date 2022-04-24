package com.fx67ll.springboot.controller;

import com.fx67ll.springboot.Starter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class})
@AutoConfigureMockMvc
public class TestUserController {

    // 日志的使用
    private Logger logger = LoggerFactory.getLogger(TestUserController.class);

    @Autowired
    private MockMvc mockMvc;

    /**
     * 模拟测试用户列表查询
     * 其实就在模拟真实环境下前端对后端发起的请求
     */
    @Test
    public void apiTestSelectUserListByParams() throws Exception {

        logger.info("开始模拟发送查询用户列表的请求......");

        // 构建请求
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/list")
                .contentType("text/html") // 设置请求头信息
                .accept(MediaType.APPLICATION_JSON); // 设置请求Accept头信息

        // 发送请求
        ResultActions perform = mockMvc.perform(requestBuilder);

        // 校验请求结果
        perform.andExpect(MockMvcResultMatchers.status().isOk());

        // 获取执行完成后返回的结果
        MvcResult mvcResult = perform.andReturn();

        // 得到执行后的响应
        MockHttpServletResponse response = mvcResult.getResponse();

        // 打印结果
        logger.info(String.valueOf(response.getContentLength()));
        logger.info("响应状态: ", response.getStatus());
        logger.info("响应信息: ", response.getContentAsString());

        logger.info("结束模拟发送查询用户列表的请求......");
    }

    @Test
    public void apiTestQueryUserByUsername() throws Exception {

        logger.info("开始模拟根据用户名查询用户记录的请求......");

        // 构建请求并发送
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/name/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        // 打印结果
        logger.info("响应状态: ", mvcResult.getResponse().getStatus());
        logger.info("响应信息: ", mvcResult.getResponse().getContentAsString());

        logger.info("结束模拟根据用户名查询用户记录的请求......");
    }
}

