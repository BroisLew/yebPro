package com.lew.server.config.filter;

import com.lew.server.utils.GeneratorTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.tokenHeader}")
    private String jwtHeader;
    @Value("${jwt.tokenHead}")
    private String jwtHead;
    @Autowired
    private GeneratorTokenUtils generatorTokenUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = httpServletRequest.getHeader(jwtHeader);
        if(tokenHeader != null && tokenHeader.startsWith(jwtHead)) {
            String token = tokenHeader.substring(jwtHead.length());
            if(token !=null&&!"".equals(token)) {
                boolean isExpiration = false;
                try{
                    isExpiration = generatorTokenUtils.isTokenExpired(token);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                String username = generatorTokenUtils.getUsernameFromToken(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (generatorTokenUtils.isValidateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new
                                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
