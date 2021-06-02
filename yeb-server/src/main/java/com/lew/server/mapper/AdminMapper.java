package com.lew.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lew.server.pojo.Admin;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> getAdmins(String keywords);

}
