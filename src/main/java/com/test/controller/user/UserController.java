package com.test.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.test.pojo.User;
import com.test.service.user.UserService;
import com.test.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/jsp/user.do")
public class UserController {

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;


    @RequestMapping(params = "method=pwdmodify")
    @ResponseBody
    public String modifyPwd(String oldpassword, HttpSession session) {
        oldpassword = DigestUtils.md5DigestAsHex(oldpassword.getBytes());
        User user = (User) session.getAttribute(Constant.USER_SESSION);
        Map<String, String> resultMap = new HashMap<String, String>();
        if (user == null) {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {
            resultMap.put("result", "error");
        } else {
            String userPassword = user.getUserPassword();
            if (oldpassword.equals(userPassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }


    @PostMapping(params = "method=modifyexe")
    public String userModify(User user, HttpSession session, RedirectAttributes attr) {
        user.setModifyBy(((User) session.getAttribute(Constant.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        boolean isUpdate = userService.updateUser(user);
        if (isUpdate) {
            attr.addAttribute("method", "query");
            return "redirect:/jsp/user.do";
        } else {
            return "usermodify";
        }

    }

    @PostMapping(params = "method=add")
    private String addUser(User user, HttpSession session, RedirectAttributes attr) {
        user.setCreationDate(new Date());
        user.setCreatedBy(((User) session.getAttribute(Constant.USER_SESSION)).getId());
        String userPassword = DigestUtils.md5DigestAsHex(user.getUserPassword().getBytes());
        user.setUserPassword(userPassword);
        int update = userService.addUser(user);
        if (update > 0) {
            attr.addAttribute("method", "query");
            return "redirect:/jsp/user.do";
        } else {
            return "useradd";
        }
    }

    @RequestMapping(params = "method=savepwd")
    private String updatePwd(String newpassword, HttpSession session, Model model) {
        Object user = session.getAttribute(Constant.USER_SESSION);

        if (user != null && !StringUtils.isNullOrEmpty(newpassword)) {
            Integer id = ((User) user).getId();
            boolean isUpdate = userService.updatePwd(id, DigestUtils.md5DigestAsHex(newpassword.getBytes()));
            if (isUpdate) {
                model.addAttribute("message", "修改密码成功");
                session.removeAttribute(Constant.USER_SESSION);
            } else {
                model.addAttribute("message", "修改密码失败");
            }
        } else {
            model.addAttribute("message", "error");
        }
        return "pwdmodify";
    }

    @RequestMapping(params = "method=query")
    private String queryUser(HttpServletRequest req, Model model) {
        String queryUserName = req.getParameter("queryname");
        String tempQueryUserRole = req.getParameter("queryUserRole");
        String tempCurrentPageNo = req.getParameter("pageIndex");

        int pageSize = Constant.PAGESIZE;
        int queryUserRole = 0;

        int currentPageNo = tempCurrentPageNo == null ? 1 : Integer.parseInt(tempCurrentPageNo);
        if (tempQueryUserRole != null && !tempQueryUserRole.equals("")) {
            queryUserRole = Integer.parseInt(tempQueryUserRole);
        }

        queryUserName = queryUserName == null ? "" : queryUserName;

        Map<String, Object> result = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);

        model.addAllAttributes(result);

        return "userlist";
    }


    @RequestMapping(params = "method=view")
    private String getUserView(String uid, Model model) {
        if (!StringUtils.isNullOrEmpty(uid)) {
            User user = userService.getUserView(Integer.parseInt(uid));
            model.addAttribute("user", user);
        }
        return "userview";
    }

    @RequestMapping(params = "method=modify")
    private String userModify(String uid, Model model) {
        if (!StringUtils.isNullOrEmpty(uid)) {
            User user = userService.getUserView(Integer.parseInt(uid));
            model.addAttribute("user", user);
        }
        return "usermodify";
    }

    @RequestMapping(params = "method=ucexist",
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    private String userExist(String userCode) {
        int count = userService.getUserCountByUserCode(userCode);
        Map<String, String> resultMap = new HashMap();
        if (count > 0)
            resultMap.put("userCode", "exist");
        else
            resultMap.put("userCode", "canbeused");

        return JSONArray.toJSONString(resultMap);
    }


    @RequestMapping(params = "method=deluser",
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    private String deleteUser(@RequestParam("uid") String userid) {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(userid)) {
            boolean isDelete = userService.deleteUser(Integer.parseInt(userid));
            if (isDelete) {
                resultMap.put("delResult", "true");
            } else
                resultMap.put("delResult", "false");
        } else {
            resultMap.put("delResult", "notexist");
        }
        return JSONArray.toJSONString(resultMap);
    }

}
