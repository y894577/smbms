package com.test.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.user.UserDao;
import com.test.pojo.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class UserServiceImpl implements UserService {
    private SqlSessionTemplate sqlSession = null;
    private UserDao userMapper = null;

    public UserServiceImpl() {

    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        this.userMapper = sqlSession.getMapper(UserDao.class);
    }

    public User login(String userCode, String userPassword) {
        User user = null;
        user = userMapper.getLoginUser(userCode, userPassword);
        return user;
    }

    public boolean updatePwd(int id, String password) {
        boolean isUpdate = false;
        if (userMapper.updatePwd(id, password) > 0) {
            isUpdate = true;
        }
        return isUpdate;
    }

    public int getUserCount(String userName, int userRole) {
        int count = 0;
        count = userMapper.getUserCount(userName, userRole);
        return count;
    }

    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        if (!userName.equals(""))
            userName = "%" + userName + "%";

        PageHelper.startPage(currentPageNo, pageSize);
        List<User> userList = userMapper.getUserList(userName, userRole);
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        return pageInfo.getList();
    }

    public User getUserView(int id) {
        User user = null;
        user = userMapper.getUserView(id);
        return user;
    }

    public boolean updateUser(User user) {
        boolean isUpdate = false;
        int update = userMapper.updateUser(user);
        if (update > 0) {
            isUpdate = true;
        }
        return isUpdate;
    }

    public int addUser(User user) {
        int update = 0;
        update = userMapper.addUser(user);
        return update;
    }

    public int getUserCountByUserCode(String userCode) {
        int count = 0;
        count = userMapper.getUserCountByUserCode(userCode);
        return count;
    }

    public boolean deleteUser(int id) {
        boolean isDelete = false;
        int i = userMapper.deleteUser(id);
        System.out.println("delete" + i);
        if (i > 0) {
            isDelete = true;
        }
        return isDelete;
    }

    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserServiceImpl userService = (UserServiceImpl) context.getBean("UserServiceImpl");
//        UserService userService = new UserServiceImpl();
        userService.getUserCount("", 0);
    }
}
