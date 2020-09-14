package com.test.dao.user;

import com.test.pojo.Role;
import com.test.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
    //获取登录的用户
    public User getLoginUser(@Param("userCode")String userCode, @Param("userPassword")String userPassword);

    //修改用户密码
    public int updatePwd(Connection connection, int id, String updatePassword);

    //查询用户总数
    public int getUserCount(Connection connection, String userName, int userRole);

    //获取用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize);

    public User getUserView(Connection connection, int id);

    public int updateUser(Connection connection, User user);

    public int addUser(Connection connection,User user);

    public int getUserCountByUserCode(Connection connection, String userCode);
}
