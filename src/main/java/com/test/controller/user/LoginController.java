package com.test.controller.user;

import com.test.pojo.User;
import com.test.service.user.UserService;
import com.test.service.user.UserServiceImpl;
import com.test.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login.do")
public class LoginController {

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @PostMapping
    public String login(String userCode, String userPassword, HttpSession session, Model model) {
        User user = userService.login(userCode, userPassword);

        if (user != null) {
            session.setAttribute(Constant.USER_SESSION, user);
            return "frame";
        } else {
            model.addAttribute("error", "fail to login");
            return "login";
        }
    }

}
