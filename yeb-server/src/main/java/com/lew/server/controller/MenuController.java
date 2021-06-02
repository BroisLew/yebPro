package com.lew.server.controller;


import com.lew.server.pojo.Menu;
import com.lew.server.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/home")
@Api(tags = "系统菜单")
public class MenuController {
    @Autowired
    private IMenuService menuService;
    @GetMapping(value = "/menus",produces = "application/json")
    @ApiOperation(value = "获取菜单列表")
    public List<Menu> menus(){
        List<Menu> menus = menuService.getMenusByAdminId();
        //List<Menu> menus = menuService.getMenusWithRole();
        return menus;
    }
}