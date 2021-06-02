package com.lew.server.config;

import com.lew.server.config.filter.CustomAccessDecisionManager;
import com.lew.server.config.filter.CustomFilter;
import com.lew.server.config.filter.TokenAuthenticationFilter;
import com.lew.server.config.handler.ResultAccessDeniedHandler;
import com.lew.server.config.handler.ResultAuthenticationEntryPoint;
import com.lew.server.pojo.Admin;
import com.lew.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private ResultAccessDeniedHandler resultAccessDeniedHandler;
    @Autowired
    private ResultAuthenticationEntryPoint resultAuthenticationEntryPoint;
    @Autowired
    private CustomAccessDecisionManager customAccessDecisionManager;
    @Autowired
    private CustomFilter customFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //解决跨域问题
        //http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        http.cors();

        //使用Token，不需要csrf
        http.csrf()
                .disable()
                //基于Token，不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(customFilter);
                        o.setAccessDecisionManager(customAccessDecisionManager);
                        return o;
                    }
                })
                .anyRequest()
                .authenticated()
                .and()
                //不需要缓存
                .headers()
                .cacheControl();

        //添加自定义Jwt登录授权过滤器
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加未授权和未登录时的自定义返回结果
        http.exceptionHandling()
                .accessDeniedHandler(resultAccessDeniedHandler)
                .authenticationEntryPoint(resultAuthenticationEntryPoint);
    }

    /**
     * 自定义获取UserDetails对象
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username->{
            Admin admin = adminService.getAdminInfoByUsername(username);
            if (admin == null){
                return null;
            }
            admin.setRoles(adminService.getRoles(admin.getId()));
            return admin;
        };
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/user/login","/user/logout","/doc.html","/captcha",
                "/css/**",
                "/js/**",
                "favicon.ico",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs/**","/index.html");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter authenticationFilter(){
        return new TokenAuthenticationFilter();
    }
}
