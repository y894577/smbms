package com.test.dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author Magic Gunner
 * @description 操作数据的公共类
 */
public class BaseDao {

    private static SqlSessionFactory sqlSessionFactory;
    static SqlSession sqlSession = null;

    static {
        Properties properties = new Properties();
        ClassLoader classLoader = BaseDao.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream("db.properties");

        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            //获取工厂类对象
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static SqlSession getSqlSession() {
        sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }


    /**
     * @param connection
     * @param preparedStatement
     * @param resultSet
     * @return true关闭成功，false关闭失败
     * @description 关闭公共方法
     */
    public static boolean close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        boolean isClose;
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
            isClose = true;
        } catch (SQLException e) {
            e.printStackTrace();
            isClose = false;
        }
        return isClose;
    }
}
