package com.lew.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.server.mapper.MenuMapper;
import com.lew.server.pojo.Admin;
import com.lew.server.pojo.Menu;
import com.lew.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public List<Menu> getMenusByAdminId() {
        Integer adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        BoundValueOperations<String, Object> valueOperations = redisTemplate.boundValueOps("menu_" + adminId);
        List<Menu> menus= (List<Menu>) valueOperations.get();

        if(menus == null){
            menus = menuMapper.getMenusByAdminId(adminId);
            valueOperations.set(menus);
        }

        return menus;
    }

    public List<Menu> getMenusWithRole(){
        return menuMapper.getMenusWithRole();
    }

    @Override
    public List<Menu> getMenus() {
        return menuMapper.getMenus();
    }
}
