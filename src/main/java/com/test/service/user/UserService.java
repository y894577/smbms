package com.test.service.user;

import com.test.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserService {

    public User login(String userCode, String password);

    public boolean updatePwd(int id, String password);

    public int getUserCount(String userName, int userRole);

    public List<User> getUserList(String userName,int userRole,int currentPageNo,int pageSize);

    public User getUserView(int id);

    public boolean updateUser(User user);

    public int addUser(User user);

    public int getUserCountByUserCode(String userCode);
}
