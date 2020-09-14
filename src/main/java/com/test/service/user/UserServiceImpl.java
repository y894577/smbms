package com.test.service.user;

import com.test.dao.BaseDao;
import com.test.dao.user.UserDao;
import com.test.dao.user.UserDaoImpl;
import com.test.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    private SqlSession sqlSession = null;
    private UserDao mapper = null;

    public UserServiceImpl() {
//        userDao = new UserDaoImpl();
        sqlSession = BaseDao.getSqlSession();
        mapper = sqlSession.getMapper(UserDao.class);
    }

    public User login(String userCode, String userPassword) {
        User user = null;

        try {
//            connection = BaseDao.getConnection();
//            user = userDao.getLoginUser(connection, userCode, userPassword);
            mapper = sqlSession.getMapper(UserDao.class);
            user = mapper.getLoginUser(userCode, userPassword);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return user;
    }

    public boolean updatePwd(int id, String password) {
        Connection connection = null;
        boolean isUpdate = false;
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePwd(connection, id, password) > 0) {
                isUpdate = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return isUpdate;
    }

    public int getUserCount(String userName, int userRole) {
        int count = 0;
        try {
            Connection connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, userName, userRole);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, userName, userRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return userList;
    }

    public User getUserView(int id) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getUserView(connection, id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return user;
    }

    public boolean updateUser(User user) {
        boolean isUpdate = false;

        try {
            Connection connection = BaseDao.getConnection();
            if (connection != null) {
                int update = userDao.updateUser(connection, user);
                if (update > 0) {
                    isUpdate = true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isUpdate;
    }

    public int addUser(User user) {
        int update = 0;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            if (connection != null) {
                update = userDao.addUser(connection, user);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return update;
    }

    public int getUserCountByUserCode(String userCode) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            if (connection != null) {
                count = userDao.getUserCountByUserCode(connection, userCode);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return count;
    }

    @Test
    public void test() {
//        UserServiceImpl userService = new UserServiceImpl();
//        System.out.println(userService.getUserCountByUserCode("aaa"));
    }
}
