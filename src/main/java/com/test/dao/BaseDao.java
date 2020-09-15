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
    private static final String driver;
    private static final String url;
    private static final String username;
    private static final String password;

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

        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");


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

    //获取数据库的链接
    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        Connection connection = null;
        Class.forName(driver);
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    /**
     * @param connection
     * @param sql        执行的sql语句
     * @param params     查询的参数
     * @return 查询的结果
     * @throws SQLException
     * @description 查询公共方法
     */
    public static ResultSet execute(Connection connection, String sql, Object[] params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int index = 0;
        for (Object param : params) {
            preparedStatement.setObject(++index, param);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    /**
     * @param connection
     * @param sql
     * @param params
     * @return 影响的行数
     * @throws SQLException
     * @description 增删改公共方法
     */
    public static int update(Connection connection, String sql, Object[] params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        int index = 0;
        for (Object param : params) {
            preparedStatement.setObject(++index, param);
        }
        int updateRows = preparedStatement.executeUpdate();
        return updateRows;
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
