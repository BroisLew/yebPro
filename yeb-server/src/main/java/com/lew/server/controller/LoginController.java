package com.lew.server.controller;

import com.lew.server.pojo.common.RespBean;
import com.lew.server.pojo.login.User;
import com.lew.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Api(tags = "登录模块")
public class LoginController {
    @Autowired
    private IAdminService adminService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录" )
    public RespBean login(@RequestBody User user, HttpServletRequest request){
        System.out.println("111"+user.getCode());
        return adminService.login(user.getUsername(),user.getPassword(),user.getCode(),request);
    }

    @ApiOperation(value = "退出登录")
    @PostMapping
    public RespBean logout(){
        return RespBean.success("退出登录");
    }
}
