package com.lew.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lew.server.pojo.Menu;
import com.lew.server.pojo.MenuRole;
import com.lew.server.pojo.Role;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.service.IMenuRoleService;
import com.lew.server.service.IMenuService;
import com.lew.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "权限组")
@RestController("/system/basic/role")
public class PermissionController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "查询所有角色")
    @GetMapping("/allRoles")
    public RespBean getAllRoles(){
        List<Role> roles = roleService.list();
        return RespBean.success("查询成功",roles);
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/addRole")
    public RespBean addRole(@RequestBody Role role){
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if(roleService.save(role)){
            return RespBean.success("保存成功");
        }
        return RespBean.error("保存失败");
    }

    @DeleteMapping("/deleteRole/{rid}")
    @ApiOperation(value = "删除角色")
    public RespBean deleteRole(@PathVariable Integer rid){
        if(roleService.removeById(rid)){
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @GetMapping("/allMenus")
    @ApiOperation(value = "查询菜单")
    public RespBean allMenus(){
        List<Menu> menus =menuService.getMenus();
        return RespBean.success("查询成功",menus);
    }

    @GetMapping("/mids/{rid}")
    @ApiOperation(value = "通过角色ID查询所有对应的菜单ID")
    public RespBean getMidByRid(@PathVariable Integer rid){
        List<Integer> mids = menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",rid)).stream()
                .map(MenuRole::getMid).collect(Collectors.toList());
        return RespBean.success("查询成功",mids);
    }

    @ApiOperation(value = "修改角色权限")
    @PutMapping("/upadteMenu")
    public RespBean updateRoleMenu(Integer rid,Integer[] mids){
        return menuRoleService.updateRoleMenu(rid,mids);
    }
}
