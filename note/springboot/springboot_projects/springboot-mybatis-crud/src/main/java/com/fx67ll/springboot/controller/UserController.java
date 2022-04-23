package com.fx67ll.springboot.controller;

import com.fx67ll.springboot.exceptions.ParamsException;
import com.fx67ll.springboot.po.User;
import com.fx67ll.springboot.po.vo.ResultInfo;
import com.fx67ll.springboot.query.UserQuery;
import com.fx67ll.springboot.srevice.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 根据处理结果向客户端发送响应的controller层
 * <p>
 * Swagger2注解说明
 * Api注解主要是用在请求类上，用于说明该类的作用
 * ApiOperation注解主要是用在请求的方法上，说明方法的作用
 * ApiImplicitParams、ApiImplicitParam主要是用在请求的方法上，说明方法的参数
 */
@Api(tags = "用户模块")
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "根据用户名查询用户对象",
            notes = "用户名不为空")
    @ApiImplicitParam(name = "userName", value = "用户名称", required = true, paramType = "path", dataType = "String", defaultValue = "")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 578, message = "请求参数错误"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    /**
     * 根据用户名查询用户对象
     *
     * @param userName
     * @return
     */
    @GetMapping("/user/{userName}")
    public User queryUserByUsername(@PathVariable String userName) {
        return userService.queryUserByUsername(userName);
    }

    @ApiOperation(value = "根据用户ID查询用户对象",
            notes = "用户ID不为空")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "Integer")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 578, message = "请求参数错误"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    /**
     * 根据用户ID查询用户对象
     * @param userId
     * @return
     */
    @GetMapping("/user/id/{userId}")
    public User queryUserId(@PathVariable Integer userId) {
        return userService.queryUserById(userId);
    }

    @ApiOperation(value = "添加用户",
            notes = "用户名和密码不为空，且用户名不能重复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "body", dataType = "String"),
            @ApiImplicitParam(name = "userPwd", value = "用户密码", required = true, paramType = "body", dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 578, message = "请求参数错误"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    /**
     * 添加用户
     * RequestBody注解表示传递过来的参数是JSON对象，否则则是通过这样的方式传递 “/adduser?userName=xxx&userPwd=xxx”
     * @param user
     * @return
     */
    @PutMapping("/adduser")
    public ResultInfo saveUser(@RequestBody User user) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            userService.saveUser(user);
        } catch (ParamsException e) {
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
            e.printStackTrace();
        } catch (Exception e) {
            resultInfo.setCode(578);
            resultInfo.setMsg("添加失败！");
            e.printStackTrace();
        }

        return resultInfo;
    }

    @ApiOperation(value = "修改用户",
            notes = "用户名和密码不为空，且不同ID的用户名不能重复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "body"),
            @ApiImplicitParam(name = "userPwd", value = "用户密码", required = true, paramType = "body"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 578, message = "请求参数错误"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    /**
     * 修改用户
     * @param user
     * @return
     */
    @PostMapping("/updateuser")
    public ResultInfo updateUser(@RequestBody User user) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            userService.updateUser(user);
        } catch (ParamsException e) {
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
            e.printStackTrace();
        } catch (Exception e) {
            resultInfo.setCode(578);
            resultInfo.setMsg("修改失败！");
            e.printStackTrace();
        }

        return resultInfo;
    }

    @ApiOperation(value = "删除用户",
            notes = "用户记录必须存在")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "Integer")
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 578, message = "请求参数错误"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    /**
     * 删除用户
     * @param userId
     * @return
     */
    @DeleteMapping("/user/{userId}")
    public ResultInfo deleteUserById(@PathVariable Integer userId) {
        ResultInfo resultInfo = new ResultInfo();

        try {
            userService.deleteUserById(userId);
        } catch (ParamsException e) {
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
            e.printStackTrace();
        } catch (Exception e) {
            resultInfo.setCode(578);
            resultInfo.setMsg("删除失败！");
            e.printStackTrace();
        }

        return resultInfo;
    }

    @ApiOperation(value = "根据条件，分页查询用户列表",
            notes = "分页参数不为空，分别是当前页和每页条数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, paramType = "path", dataType = "Integer", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, paramType = "path", dataType = "Integer", defaultValue = "10"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功"),
            @ApiResponse(code = 578, message = "请求参数错误"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    /**
     * 根据条件，分页查询用户列表
     * @param userQuery
     * @return
     */
    @GetMapping("/user/list")
    public PageInfo<User> selectUserListByParams(UserQuery userQuery) {
        return userService.selectUserListByParams(userQuery);
    }
}
