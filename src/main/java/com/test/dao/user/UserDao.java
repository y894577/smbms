package com.test.dao.user;

import com.test.pojo.Role;
import com.test.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
    //获取登录的用户
    public User getLoginUser(Connection connection, String userCode, String userPassword);

    //修改用户密码
    public int updatePwd(Connection connection, int id, String updatePassword);

    //查询用户总数
    public int getUserCount(Connection connection, String userName, int userRole);

    //获取用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize);
}
