package com.lew.server.controller;


import com.lew.server.pojo.Admin;
import com.lew.server.pojo.Role;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.service.IAdminService;
import com.lew.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "用户信息模块")
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;

    @GetMapping("/userInfo")
    @ApiOperation(value = "获取用户信息")
    public Admin userInfo(Principal principal){
        if(principal == null){
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminInfoByUsername(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }
    @ApiOperation(value = "获取所有职员信息")
    @GetMapping("/get")
    public List<Admin> getAdmins(String keywords){
        return adminService.getAdmins(keywords);
    }

    @ApiOperation(value = "更新职员信息")
    @PutMapping("/update")
    public RespBean updateAdminInfo(@RequestBody Admin admin){
        if(adminService.updateById(admin)){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除职员信息")
    public RespBean deleteAdmin(@PathVariable("id") int id){
        if(adminService.removeById(id)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "获取职员所有权利")
    @GetMapping("/roles")
    public List<Role> getRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "授予职员权力")
    @PutMapping("/authority")
    public RespBean updateAuthority(int id,int[] rids){
        return adminService.updateAuthority(id,rids);
    }
}
