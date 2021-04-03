package com.test.service.user;

import com.test.pojo.User;


import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface UserService {

    User login(String userCode, String password) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    boolean updatePwd(int id, String password);

    int getUserCount(String userName, int userRole);

    Map<String,Object> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    User getUserView(int id);

    boolean updateUser(User user);

    int addUser(User user);

    int getUserCountByUserCode(String userCode);

    boolean deleteUser(int id);
}
