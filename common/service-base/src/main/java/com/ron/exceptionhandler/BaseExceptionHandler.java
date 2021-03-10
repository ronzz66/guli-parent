package com.ron.exceptionhandler;


import com.ron.commonutils.ExceptionUtil;
import com.ron.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice//异常处理
@Slf4j
public class BaseExceptionHandler {
    //全局捕获异常
    @ExceptionHandler(value = Exception.class)//异常处理对象
    @ResponseBody
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("执行全局异常处理");
    }

    //特殊异常处理
    @ExceptionHandler(value = ArithmeticException.class)//异常处理对象
    @ResponseBody
    public R error(ArithmeticException e) {
        e.printStackTrace();
        return R.error().message("执行ArithmeticException异常处理");
    }

    //自定义异常处理，系统不认识自定义的异常所以           需要手动抛出
    @ExceptionHandler(value = GuliException.class)//异常处理对象
    @ResponseBody
    public R error(GuliException e) {
        log.error(ExceptionUtil.getMessage(e));//将错误日志信息输出到文件
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}