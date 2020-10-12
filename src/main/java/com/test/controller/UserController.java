package com.test.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.test.pojo.User;
import com.test.service.user.UserService;
import com.test.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/jsp/user.do")
public class UserController {

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;


    @RequestMapping("/pwdmodify")
    @ResponseBody
    public String modifyPwd(Model model, HttpServletRequest req) throws IOException {
        User user = (User) req.getSession().getAttribute(Constant.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<String, String>();
        if (user != null) {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty("oldpassword")) {
            resultMap.put("result", "error");
        } else {
            String userPassword = ((User) user).getUserPassword();
            if (oldpassword.equals(userPassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }
        String result = JSONArray.toJSONString(resultMap);
        return result;
    }

    @PostMapping(params = "method = modifyexe")
    public void userModify(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setUserName(userName);
        user.setGender(Integer.parseInt(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User) req.getSession().getAttribute(Constant.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        boolean isUpdate = userService.updateUser(user);
        try {
            if (isUpdate) {
                resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
            } else {
                req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping(params = "method = add")
    private String addUser(User user, RedirectAttributes attr) {

        int update = userService.addUser(user);
        if (update > 0) {
            attr.addAttribute("method", "query");
            return "redirect:/jsp/user.do";
        } else {
            return "";
        }
    }

    @RequestMapping(params = "method = savepwd")
    private String updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object user = req.getSession().getAttribute(Constant.USER_SESSION);

        String newpassword = req.getParameter("newpassword");
        if (user != null && !StringUtils.isNullOrEmpty(newpassword)) {
            Integer id = ((User) user).getId();
            boolean isUpdate = userService.updatePwd(id, newpassword);
            if (isUpdate) {
                req.setAttribute("message", "修改密码成功");
                req.getSession().removeAttribute(Constant.USER_SESSION);
            } else {
                req.setAttribute("message", "修改密码失败");
            }
        } else {
            req.setAttribute("message", "error");
        }
        return "pwdmodify";
    }



}
