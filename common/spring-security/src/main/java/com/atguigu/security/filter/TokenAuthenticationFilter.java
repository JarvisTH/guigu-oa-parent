package com.atguigu.security.filter;

import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.utils.ResponseUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("uri:" + request.getRequestURI());
        //如果是登录接口，直接放行
        if ("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 请求头是否有token
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            if (!StringUtils.isEmpty(username)) {
                return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            }
        }
        return null;
    }
}
