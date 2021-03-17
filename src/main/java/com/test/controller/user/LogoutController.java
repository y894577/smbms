package com.test.controller.user;

import com.test.pojo.User;
import com.test.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/jsp/logout.do")
public class LogoutController {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @RequestMapping
    private String logout(HttpSession session) {
        User user =(User) session.getAttribute(Constant.USER_SESSION);
        redisTemplate.delete("loginUser:" + user.getUserCode());
        session.removeAttribute(Constant.USER_SESSION);
        return "redirect:/login.jsp";
    }
}
