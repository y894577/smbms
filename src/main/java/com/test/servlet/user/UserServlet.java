package com.test.servlet.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.test.dao.BaseDao;
import com.test.pojo.Role;
import com.test.pojo.User;
import com.test.service.role.RoleService;
import com.test.service.role.RoleServiceImpl;
import com.test.service.user.UserService;
import com.test.service.user.UserServiceImpl;
import com.test.util.Constant;
import com.test.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("modifyexe")) {
            this.userModify(req, resp);
        } else if (method.equals("add")) {
            this.addUser(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null) {
            if (method.equals("pwdmodify")) {
                //验证旧密码
                this.modifyPwd(req, resp);
            } else if (method.equals("savepwd")) {
                //更新新密码
                this.updatePwd(req, resp);
            } else if (method.equals("query")) {
                this.query(req, resp);
            } else if (method.equals("view")) {
                this.view(req, resp, "userview.jsp");
            } else if (method.equals("modify")) {
                this.view(req, resp, "usermodify.jsp");
            } else if (method.equals("getrolelist")) {
                this.getRoleList(req, resp);
            } else if (method.equals("ucexist")) {
                this.userExist(req,resp);
            }
        }
    }

    private void userModify(HttpServletRequest req, HttpServletResponse resp) {
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

        UserService userService = new UserServiceImpl();
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

    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object user = req.getSession().getAttribute(Constant.USER_SESSION);

        String newpassword = req.getParameter("newpassword");
        if (user != null && !StringUtils.isNullOrEmpty(newpassword)) {
            user = (User) user;
            Integer id = ((User) user).getId();
            UserService userService = new UserServiceImpl();
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
        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }

    private void modifyPwd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object user = req.getSession().getAttribute(Constant.USER_SESSION);
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

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    private void query(HttpServletRequest req, HttpServletResponse resp) {
        String queryUserName = req.getParameter("queryname");
        String tempQueryUserRole = req.getParameter("queryUserRole");
        String tempCurrentPageNo = req.getParameter("pageIndex");
        int pageSize = 5;
        int queryUserRole = 0;
        UserService userService = new UserServiceImpl();

        queryUserName = queryUserName == null ? "" : queryUserName;
        int currentPageNo = tempCurrentPageNo == null ? 1 : Integer.parseInt(tempCurrentPageNo);
        if (tempQueryUserRole != null && !tempQueryUserRole.equals("")) {
            queryUserRole = Integer.parseInt(tempQueryUserRole);
        }

        int totalCount = userService.getUserCount(queryUserName, queryUserRole);

        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        //使用工具类获取总页数
        int totalPageCount = pageSupport.getTotalPageCount();

        if (currentPageNo < 1) {
            //如果页面小于1，则显示第一页的东西
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            //如果页面大于当前页面，则显示最后一页
            currentPageNo = totalPageCount;
        }

        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);

        //获取角色列表
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

        req.setAttribute("userList", userList);
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);

        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void view(HttpServletRequest req, HttpServletResponse resp, String url) {
        String id = req.getParameter("uid");
        UserService userService = new UserServiceImpl();
        if (!StringUtils.isNullOrEmpty(id)) {
            User user = userService.getUserView(Integer.parseInt(id));
            try {
                req.setAttribute("user", user);
                req.getRequestDispatcher(url).forward(req, resp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    private void userModify(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        String id = req.getParameter("uid");
        UserService userService = new UserServiceImpl();
        if (!StringUtils.isNullOrEmpty(id)) {
            User user = userService.getUserView(Integer.parseInt(id));
            try {
                req.setAttribute("user", user);
                req.getRequestDispatcher(url).forward(req, resp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    private void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(roleList));
        writer.flush();
        writer.close();
    }


    private void addUser(HttpServletRequest req, HttpServletResponse resp) {
        User user = null;
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.parseInt(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreatedBy(((User) req.getSession().getAttribute(Constant.USER_SESSION)).getId());
        user.setCreationDate(new Date());

        UserService userService = new UserServiceImpl();
        int update = userService.addUser(user);
        if (update > 0) {
            try {
                resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void userExist(HttpServletRequest req, HttpServletResponse resp) {
        String userCode = req.getParameter("userCode");
        UserService userService = new UserServiceImpl();
        int count = userService.getUserCountByUserCode(userCode);
        Map<String, String> resultMap = new HashMap<String, String>();
        if (count > 0) {
            resultMap.put("userCode", "exist");
        }else {
            resultMap.put("userCode","canbeused");
        }
        resp.setContentType("application/json;charset=utf-8");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
