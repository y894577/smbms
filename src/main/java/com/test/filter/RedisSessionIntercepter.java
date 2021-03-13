package com.test.filter;

import com.test.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class RedisSessionIntercepter extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplate redisTemplate;

    private List<String> excludeUrls;

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        boolean doFilter = true;
        for (String s : excludeUrls) {
            if (uri.contains(s)) {
                doFilter = false;
            }
            break;
        }
        if (doFilter) {
            HttpSession session = request.getSession();
            if (session.getAttribute(Constant.USER_SESSION) != null) {
                String loginSessionId = (String) redisTemplate.opsForValue().get("loginUser:" + session.getAttribute(Constant.USER_SESSION));
                if (loginSessionId != null && loginSessionId.equals(session.getId()))
                    return true;
            }
        }
        return false;
    }
}
