package com.fx67ll.springboot.exceptions;

/**
 * 自定义异常
 */
public class ParamsException extends RuntimeException {
    private Integer code = 578;
    private String msg = "参数异常";

    public ParamsException() {
        super("参数异常");
    }

    public ParamsException(Integer code) {
        super("参数异常");
        this.code = code;
    }

    public ParamsException(String msg) {
        super("参数异常");
        this.msg = msg;
    }

    public ParamsException(Integer code, String msg) {
        super("参数异常");
        this.code = code;
        this.msg = msg;
    }

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
}
