package com.test.filter;

import com.test.pojo.User;
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
    private RedisTemplate<String, Object> redisTemplate;

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
            User user = (User) session.getAttribute(Constant.USER_SESSION);
            if (user != null) {
                Integer loginSessionId = (Integer) redisTemplate.opsForHash().get("loginUser:" + user.getId(), "id");
                if (loginSessionId != null && loginSessionId.equals(user.getId()))
                    return true;
            }
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return false;
        }
        return true;
    }
}
