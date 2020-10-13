package com.test.controller.user;

import com.test.pojo.User;
import com.test.service.user.UserService;
import com.test.service.user.UserServiceImpl;
import com.test.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login.do")
public class LoginController {

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @PostMapping
    public String login(HttpServletRequest req){
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        User user = userService.login(userCode, userPassword);

        if (user != null) {
            req.getSession().setAttribute(Constant.USER_SESSION, user);
//            resp.sendRedirect("jsp/frame.jsp");
            return "frame";
        } else {
            req.setAttribute("error", "fail to login");
//            req.getRequestDispatcher("login.jps").forward(req, resp);
            return "login";
        }
    }

}
