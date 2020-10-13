package com.test.controller.user;

import com.test.util.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("logout.do")
public class LogoutController {

    @PostMapping
    private String logout(HttpSession session) {
        session.removeAttribute(Constant.USER_SESSION);
        return "redirect:/login.jsp";
    }
}
