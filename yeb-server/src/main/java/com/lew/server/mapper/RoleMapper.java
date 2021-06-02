package com.lew.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> getRoles(Integer adminId);
}
