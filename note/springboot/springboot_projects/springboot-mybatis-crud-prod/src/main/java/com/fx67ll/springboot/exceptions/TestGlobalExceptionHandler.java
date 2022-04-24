package com.fx67ll.springboot.exceptions;

import com.fx67ll.springboot.po.vo.ResultInfo;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class TestGlobalExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultInfo exceptionHandler(Exception exception) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(978);
        resultInfo.setMsg("全局异常拦截，操作失败！");
//        if (exception instanceof ParamsException) {
//            ParamsException paramsException = (ParamsException) exception;
//            resultInfo.setMsg(paramsException.getMsg());
//            resultInfo.setCode(paramsException.getCode());
//        }
        // 全局数据校验，注意！！！使用 json 请求体调用接口，校验异常抛出 MethodArgumentNotValidException
        if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
            resultInfo.setCode(1023);
            resultInfo.setMsg(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
        }
        return resultInfo;
    }

}
