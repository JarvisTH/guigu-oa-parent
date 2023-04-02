package com.atguigu.common.config.exception;

import com.atguigu.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常处理执行的方法
    @ExceptionHandler(Exception.class)
    @ResponseBody  // 为了返回json数据
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail().message("执行全局异常处理...");
    }

    // 特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.fail().message("执行特定异常处理...");
    }

    // 自定义异常处理
    @ExceptionHandler(MyTestException.class)
    @ResponseBody
    public Result error(MyTestException e) {
        e.printStackTrace();
        return Result.fail().message("执行自定义异常处理...");
    }
}
