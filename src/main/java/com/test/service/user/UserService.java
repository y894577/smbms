package com.test.service.user;

import com.test.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserService {

    User login(String userCode, String password);

    boolean updatePwd(int id, String password);

    int getUserCount(String userName, int userRole);

    List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    User getUserView(int id);

    boolean updateUser(User user);

    int addUser(User user);

    int getUserCountByUserCode(String userCode);

    boolean deleteUser(int id);
}
