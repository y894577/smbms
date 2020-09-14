package com.test;

import com.test.dao.BaseDao;
import com.test.dao.user.UserDao;
import com.test.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    @After
    public void close(){
        sqlSession.commit();
        sqlSession.close();
    }

}
