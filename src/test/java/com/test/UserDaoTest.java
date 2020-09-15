package com.test;

import com.test.dao.BaseDao;
import com.test.dao.user.UserDao;
import com.test.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.nio.cs.ext.GB18030;

import java.util.HashMap;
import java.util.List;

public class UserDaoTest {
    SqlSession sqlSession = null;
    UserDao mapper = null;

    @Before
    public void init(){
        sqlSession = BaseDao.getSqlSession();
        mapper = sqlSession.getMapper(UserDao.class);
    }
    @Test
    public void update() {
//        User user = new User();
//        user.setId(16);
//        user.setUserCode("abc");
//        user.setUserName("abc");
//        Integer i = mapper.updateUser(user);
//        System.out.println(i);
        User admin = mapper.getLoginUser("admin", "");
//        System.out.println(admin.getId());

    }

//    @Test
//    public void delete(){
//        User user = new User();
//        user.setId(16);
//        System.out.println(mapper.deleteUser(user));
//    }

//    @Test
//    public void add(){
//        User user = new User();
//        user.setId(16);
//        user.setUserCode("abc");
//        user.setUserName("abc");
//        Integer i = mapper.addUser(user);
//        System.out.println(i);
//    }

    @Test
    public void getList(){
        int userCount = mapper.getUserCount("", 0);
        System.out.println(userCount);
    }

    @Test
    public void getUserList(){
        String userName = "%"+"a"+"%";

        List<User> list = mapper.getUserList(null, 1, 1, 100);
        System.out.println(list.size());
        for (User user : list) {
            System.out.println(user.toString());
        }
    }

    @After
    public void close(){
        sqlSession.commit();
        sqlSession.close();
    }

}
