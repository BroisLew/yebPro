package com.lew.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * @param adminId
     * @return
     */
    List<Menu> getMenusByAdminId(Integer adminId);

    List<Menu> getMenusWithRole();

    List<Menu> getMenus();
}
