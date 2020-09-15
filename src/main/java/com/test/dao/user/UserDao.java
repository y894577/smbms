package com.test.dao.user;

import com.test.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
    //获取登录的用户
    User getLoginUser(@Param("userCode") String userCode,
                      @Param("userPassword") String userPassword);

    //修改用户密码
    int updatePwd(@Param("id") int id,
                  @Param("userPassword") String updatePassword);

    //查询用户总数
    int getUserCount(@Param("userName") String userName,
                     @Param("userRole") int userRole);

    //获取用户列表
    List<User> getUserList(@Param("userName") String userName,
                           @Param("userRole") Integer userRole,
                           @Param("currentPageNo") int currentPageNo,
                           @Param("pageSize") int pageSize);

    User getUserView(@Param("id") int id);

    int updateUser(User user);

    int addUser(User user);

    int getUserCountByUserCode(@Param("userCode") String userCode);
}
