package com.test.service.user;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.dao.user.UserDao;
import com.test.dao.user.UserRedisDao;
import com.test.dao.user.UserRedisDaoImpl;
import com.test.pojo.User;
import com.test.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisData;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private SqlSessionTemplate sqlSession = null;
    private UserDao userMapper = null;

    @Autowired
    private UserRedisDao userRedisDao = null;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    public UserServiceImpl() {

    }


    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        this.userMapper = sqlSession.getMapper(UserDao.class);
    }

    public User login(String userCode, String userPassword) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = null;
        user = userMapper.getLoginUser(userCode, userPassword);
        userRedisDao.add(user);
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

    public Map<String, Object> getUserList(String userName, final int userRole, int currentPageNo, int pageSize) {

        userRedisDao.get("name");
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("queryUserName", userName);
        result.put("queryUserRole", userRole);

        if (!userName.equals(""))
            userName = "%" + userName + "%";

        final String finalUserName = userName;
        PageInfo<User> pageInfo = PageHelper.startPage(0, Integer.MAX_VALUE - 1).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                userMapper.getUserList(finalUserName, userRole);
            }
        });


        //获取总条数
        int totalCount = (int) pageInfo.getTotal();
        //获取总页数
        int totalPageCount = (int) Math.ceil(totalCount * 1.0 / pageSize);
        currentPageNo = totalCount == 0 ? 0 : currentPageNo;


        pageInfo = PageHelper.startPage(currentPageNo, pageSize).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                userMapper.getUserList(finalUserName, userRole);
            }
        });

        result.put("userList", pageInfo.getList());
        result.put("totalCount", totalCount);
        result.put("currentPageNo", currentPageNo);
        result.put("totalPageCount", totalPageCount);

        return result;
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

}
