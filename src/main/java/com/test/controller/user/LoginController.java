package com.test.controller.user;

import com.test.pojo.User;
import com.test.service.user.UserService;
import com.test.service.user.UserServiceImpl;
import com.test.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping("/login.do")
public class LoginController {

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @PostMapping
    public String login(String userCode, String userPassword, HttpSession session, RedirectAttributes attr) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        userPassword = DigestUtils.md5DigestAsHex(userPassword.getBytes());
        User user = userService.login(userCode, userPassword);
        if (user != null) {
            session.setAttribute(Constant.USER_SESSION, user);
            return "frame";
        } else {
            attr.addAttribute("method", "errorlogin");
            return "redirect:/login.do";
        }
    }

    @RequestMapping(params = {"method=errorlogin"})
    public String errorLogin(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print("<script language=\"javascript\">alert('登录失败！');window.location.href='/login.jsp'</script>");
        out.flush();
        return "redirect:/login.jsp";
    }

}
