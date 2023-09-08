package com.atguigu.common.config.exception;

import lombok.Data;

@Data
public class MyTestException extends RuntimeException {
    private Integer code;
    private String msg;

    public MyTestException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MyTestException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
