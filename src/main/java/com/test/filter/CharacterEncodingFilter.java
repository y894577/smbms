package com.test.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest req=(HttpServletRequest) request;
//        HttpServletResponse resp=(HttpServletResponse) response;
//
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//
//        String URI = req.getRequestURI();
//        if (URI.contains(".css")) {
//            chain.doFilter(request, response);
//            return ;
//        }
//        response.setContentType("text/html;charset=UTF-8");
//
//        chain.doFilter(request, response);

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);

    }

    public void destroy() {

    }
}
