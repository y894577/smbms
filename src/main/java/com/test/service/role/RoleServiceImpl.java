package com.test.service.role;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.dao.role.RoleDao;
import com.test.dao.role.RoleRedisDao;
import com.test.dao.user.UserRedisDao;
import com.test.pojo.Role;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class RoleServiceImpl implements RoleService {
    private SqlSessionTemplate sqlSession = null;
    private RoleDao roleMapper = null;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RoleRedisDao roleRedisDao = null;

    public RoleServiceImpl() {
    }

    public void setSqlSession(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
        roleMapper = sqlSession.getMapper(RoleDao.class);
    }


    public Set<Role> getRoleList() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (redisTemplate.opsForSet().size("Role") == 0) {
            System.out.println("database");
            List<Role> roleList = null;
            roleList = roleMapper.getRoleList();
            Set<Role> roleSet = new HashSet<>(roleList);
            roleRedisDao.add(new ArrayList<>(roleSet));
            return roleSet;
        } else {
            System.out.println("redis");
            Set<Role> roleSet = roleRedisDao.zget();
            return roleSet;
        }
    }
}
