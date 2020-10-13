package com.test;

import com.test.dao.BaseDao;
import com.test.dao.bill.BillDao;
import com.test.dao.user.UserDao;
import com.test.pojo.Bill;
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
    BillDao billDao = null;

    @Before
    public void init(){
        sqlSession = BaseDao.getSqlSession();
        mapper = sqlSession.getMapper(UserDao.class);
        billDao = sqlSession.getMapper(BillDao.class);
    }
    @Test
    public void update() {
        User admin = mapper.getLoginUser("admin", "");

    }


    @Test
    public void getList(){
        int userCount = mapper.getUserCount("", 0);
        System.out.println(userCount);
    }

    @Test
    public void getUserList(){
        String userName = "%"+"a"+"%";

        List<User> list = mapper.getUserList(null, 1);
        System.out.println(list.size());
        for (User user : list) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void getBillCount(){
        Integer i = 1;
        int count = billDao.getBillCountByProId(i);
        System.out.println(count);
    }

    @Test
    public void getBill(){
        Integer i = 2;
        Bill bill = billDao.getBillById(2);
        System.out.println(bill.getProviderId());
    }

    @After
    public void close(){
        sqlSession.commit();
        sqlSession.close();
    }

}
