package com.test;

import com.test.controller.bill.BillController;
import com.test.dao.bill.BillDao;
import com.test.pojo.Bill;
import com.test.pojo.User;
import com.test.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RedisTest {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    class UserServiceTest implements UserService{

        @Autowired
        private UserService userService;

        @Override
        public User login(String userCode, String password) {
            return null;
        }

        @Override
        public boolean updatePwd(int id, String password) {
            return false;
        }

        @Override
        public int getUserCount(String userName, int userRole) {
            return 0;
        }

        @Override
        public Map<String, Object> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
            return null;
        }

        @Override
        public User getUserView(int id) {
            return null;
        }

        @Override
        public boolean updateUser(User user) {
            return false;
        }

        @Override
        public int addUser(User user) {
            user.setUserName("xiao ming");
            user.setAge(18);
            user.setPhone("18682329318");
            userService.addUser(user);
            return 0;
        }

        @Override
        public int getUserCountByUserCode(String userCode) {
            return 0;
        }

        @Override
        public boolean deleteUser(int id) {
            return false;
        }
    }



    @Test
    public void test() {

//        User getUser = (User) redisTemplate.opsForValue().get("user");
//        System.out.println(getUser.toString());
    }
}
