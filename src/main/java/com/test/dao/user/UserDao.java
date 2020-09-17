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
    //后续会用插件对分页进行优化，此处暂不优化
    List<User> getUserList(@Param("userName") String userName,
                           @Param("userRole") Integer userRole,
                           @Param("currentPageNo") int currentPageNo,
                           @Param("pageSize") int pageSize);

    //获取用户详细信息
    User getUserView(@Param("id") int id);

    //更新用户数据
    int updateUser(User user);

    //新增用户
    int addUser(User user);

    //通过userCode模糊查询查询用户数量
    int getUserCountByUserCode(@Param("userCode") String userCode);

    //删除用户
    int deleteUser(@Param("id") int id);
}
