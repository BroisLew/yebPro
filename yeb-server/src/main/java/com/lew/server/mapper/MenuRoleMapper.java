package com.lew.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {
    Integer insertMenuId(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
