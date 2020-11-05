package com.key.exception;

import com.key.exception.code.BaseResponseCode;

/**
 * @author 徐雨轩
 * @description 自定义运行时异常
 * @date 2020-11-05 19:09
 */
public class BusinessException extends RuntimeException {

    //提示编码
    private final int code;

    //后端提示语
    private final String msg;


    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(BaseResponseCode responseCode){
        this(responseCode.getCode(),responseCode.getMsg());
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
