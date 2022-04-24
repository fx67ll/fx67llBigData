package com.fx67ll.springboot.po.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 添加自定义返回信息
 */
@ApiModel(description = "响应信息类")
public class ResultInfo {

    @ApiModelProperty(value = "响应状态码", example = "200")
    private Integer code = 200;
    @ApiModelProperty(value = "响应信息", example = "操作成功！")
    private String msg = "操作成功！";
    @ApiModelProperty(value = "响应结果对象", example = "null")
    private Object result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
