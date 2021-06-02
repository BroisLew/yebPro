package com.lew.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lew.server.pojo.Role;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.pojo.Admin;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
public interface IAdminService extends IService<Admin> {

    RespBean login(String username, String password, String code, HttpServletRequest request);

    Admin getAdminInfoByUsername(String username);

    List<Role> getRoles(Integer adminId);

    List<Admin> getAdmins(String keywords);

    RespBean updateAuthority(int id, int[] rids);
}
