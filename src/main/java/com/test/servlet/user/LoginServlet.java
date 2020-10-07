package com.test.servlet.user;

import com.test.pojo.User;
import com.test.service.user.UserService;
import com.test.service.user.UserServiceImpl;
import com.test.util.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

//控制层
public class LoginServlet extends HttpServlet {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    UserServiceImpl userService = (UserServiceImpl) context.getBean("UserServiceImpl");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        User user = userService.login(userCode, userPassword);

        if (user != null) {
            req.getSession().setAttribute(Constant.USER_SESSION, user);
            resp.sendRedirect("jsp/frame.jsp");
        } else {
            req.setAttribute("error", "fail to login");
            req.getRequestDispatcher("login.jps").forward(req, resp);
        }
    }

}
