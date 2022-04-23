package com.fx67ll.springboot.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户查询对象
 */
@ApiModel(description = "用户列表查询类")
public class UserQuery {

    // 分页参数
    @ApiModelProperty(value = "当前页", example = "1")
    private Integer pageNum = 1;
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;

    // 条件参数
    private String userName;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
