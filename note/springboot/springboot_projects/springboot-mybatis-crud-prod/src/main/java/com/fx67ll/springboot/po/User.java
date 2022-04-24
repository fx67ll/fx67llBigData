package com.fx67ll.springboot.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel(description = "用户实体类")
public class User implements Serializable {

    @ApiModelProperty(value = "用户ID", required = true, example = "0")
    private Integer id;
    @ApiModelProperty(value = "用户名", required = true, example = "fx67ll")
    @NotBlank(message = "用户名称不能为空！")
    private String userName;
    @ApiModelProperty(value = "用户密码", required = true, example = "xxxxxxxx")
    @NotBlank(message = "用户密码不能为空！")
    @Length(min = 6, max = 20, message = "密码长度最少六位且最多二十位！")
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                '}';
    }
}
