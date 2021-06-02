package com.lew.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lew.server.mapper.AdminRoleMapper;
import com.lew.server.mapper.RoleMapper;
import com.lew.server.pojo.AdminRole;
import com.lew.server.pojo.Role;
import com.lew.server.pojo.common.RespBean;
import com.lew.server.mapper.AdminMapper;
import com.lew.server.pojo.Admin;
import com.lew.server.service.IAdminService;
import com.lew.server.utils.GeneratorTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Lew
 * @since 2021-02-28
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GeneratorTokenUtils generatorTokenUtils;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;


    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        System.out.println(code);
        System.out.println(username);
        String kaptcha =(String) request.getSession().getAttribute("kaptcha");
        System.out.println(kaptcha);
        if (code == null)
        {
            return RespBean.error("验证码不能为空");
        }
        if (!code.equals(kaptcha)){
            return RespBean.error("验证码错误，请重新输入");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails == null || passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error("用户名或密码不对");
        }
        if (!userDetails.isEnabled()){
            return RespBean.error("该用户已被禁用");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String token = generatorTokenUtils.generatorToken(userDetails);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("tokenHead",tokenHead);
        tokenMap.put("token",token);
        return RespBean.success("登录成功",tokenMap);
    }

    @Override
    public Admin getAdminInfoByUsername(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username",username)
                .eq("enabled",true));
    }

    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    @Override
    public List<Admin> getAdmins(String keywords) {
        return adminMapper.getAdmins(keywords);
    }

    @Override
    public RespBean updateAuthority(int id, int[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",id));
        if(rids.length == 0 || rids== null)
            return RespBean.success("授权成功");
        int count = adminRoleMapper.insertByAdminId(id,rids);
        if(count == rids.length)
            return RespBean.success("授权成功");
        return RespBean.error("授权失败");
    }
}
