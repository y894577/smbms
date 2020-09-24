package com.test.service.user;

import com.test.dao.BaseDao;
import com.test.dao.user.UserDao;
import com.test.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserServiceImpl implements UserService {
    private SqlSession sqlSession = null;
    private UserDao mapper = null;

    public UserServiceImpl() {
    }

    public User login(String userCode, String userPassword) {
        sqlSession = BaseDao.getSqlSession();
        User user = null;
        mapper = sqlSession.getMapper(UserDao.class);
        user = mapper.getLoginUser(userCode, userPassword);
        sqlSession.close();
        return user;
    }

    public boolean updatePwd(int id, String password) {
        sqlSession = BaseDao.getSqlSession();
        boolean isUpdate = false;
        mapper = sqlSession.getMapper(UserDao.class);
        if (mapper.updatePwd(id, password) > 0) {
            isUpdate = true;
        }
        sqlSession.close();
        return isUpdate;
    }

    public int getUserCount(String userName, int userRole) {
        sqlSession = BaseDao.getSqlSession();
        int count = 0;
        mapper = sqlSession.getMapper(UserDao.class);
        count = mapper.getUserCount(userName, userRole);
        sqlSession.close();
        return count;
    }

    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        sqlSession = BaseDao.getSqlSession();
        mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = null;
        currentPageNo = (currentPageNo - 1) * pageSize;
        if (!userName.equals(""))
            userName = "%" + userName + "%";
        userList = mapper.getUserList(userName, userRole, currentPageNo, pageSize);
        sqlSession.close();
        return userList;
    }

    public User getUserView(int id) {
        sqlSession = BaseDao.getSqlSession();
        mapper = sqlSession.getMapper(UserDao.class);
        User user = null;
        user = mapper.getUserView(id);
        sqlSession.close();
        return user;
    }

    public boolean updateUser(User user) {
        sqlSession = BaseDao.getSqlSession();
        boolean isUpdate = false;
        mapper = sqlSession.getMapper(UserDao.class);
        int update = mapper.updateUser(user);
        if (update > 0) {
            isUpdate = true;
        }
        sqlSession.commit();
        sqlSession.close();
        return isUpdate;
    }

    public int addUser(User user) {
        sqlSession = BaseDao.getSqlSession();
        int update = 0;
        mapper = sqlSession.getMapper(UserDao.class);
        update = mapper.addUser(user);
        sqlSession.close();
        return update;
    }

    public int getUserCountByUserCode(String userCode) {
        sqlSession = BaseDao.getSqlSession();
        int count = 0;
        mapper = sqlSession.getMapper(UserDao.class);
        count = mapper.getUserCountByUserCode(userCode);
        sqlSession.close();
        return count;
    }

    public boolean deleteUser(int id) {
        sqlSession = BaseDao.getSqlSession();
        boolean isDelete = false;
        mapper = sqlSession.getMapper(UserDao.class);
        int i = mapper.deleteUser(id);
        sqlSession.commit();
        System.out.println("delete" + i);
        if (i > 0) {
            isDelete = true;
        }
        sqlSession.close();
        return isDelete;
    }

    @Test
    public void test() {
        UserService userService = new UserServiceImpl();
        userService.deleteUser(9);
    }
}
