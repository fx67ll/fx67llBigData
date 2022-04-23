package com.fx67ll.springboot.utils;

import com.fx67ll.springboot.exceptions.ParamsException;

public class AssertUtils {

    /**
     * 判断结果是否为true，为true，抛出异常
     * @param flag
     * @param msg
     */
    public static void isTrue(Boolean flag, String msg) {
        if (flag){
            throw new ParamsException(msg);
        }
    }
}
