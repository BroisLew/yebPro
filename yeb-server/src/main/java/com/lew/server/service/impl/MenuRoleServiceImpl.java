package com.lew.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.server.mapper.MenuRoleMapper;
import com.lew.server.pojo.MenuRole;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.service.IMenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {
    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Override
    public RespBean updateRoleMenu(Integer rid, Integer[] mids) {
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rid));
        if(mids == null || mids.length == 0){
            return RespBean.success("修改成功");
        }
        Integer count = menuRoleMapper.insertMenuId(rid,mids);
        if(count == mids.length){
            return RespBean.success("修改成功");
        }
        return RespBean.error("修改失败");
    }
}
