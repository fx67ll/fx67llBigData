package com.fx67ll.springboot.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "用户实体类")
public class User {

    @ApiModelProperty(value = "用户名", required = true, example = "0")
    private Integer id;
    @ApiModelProperty(value = "用户ID", required = true, example = "fx67ll")
    private String userName;
    @ApiModelProperty(value = "用户密码", required = true, example = "xxxxxxxx")
    private String userPwd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

}
