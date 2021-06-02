package com.lew.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lew.server.pojo.MenuRole;
import com.lew.server.pojo.common.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface IMenuRoleService extends IService<MenuRole> {
    RespBean updateRoleMenu(Integer rid, Integer[] mids);
}
