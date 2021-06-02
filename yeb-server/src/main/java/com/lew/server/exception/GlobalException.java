package com.lew.server.exception;

import com.lew.server.pojo.common.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

//@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(SQLException.class)
    public RespBean sqlException(SQLException e){
        return RespBean.error("数据库操作异常");
    }
}
