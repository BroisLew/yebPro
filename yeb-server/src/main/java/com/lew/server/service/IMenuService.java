package com.lew.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lew.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> getMenusByAdminId();

    List<Menu> getMenusWithRole();

    List<Menu> getMenus();
}
