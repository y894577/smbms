package com.test.dao.user;

import com.mysql.jdbc.StringUtils;
import com.test.dao.BaseDao;
import com.test.pojo.Role;
import com.test.pojo.User;

import javax.sound.midi.Soundbank;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    public User getLoginUser(Connection connection, String userCode, String userPassword) {
        PreparedStatement preparedStatement = null;
        User user = null;

        //判断是否连接成功
        if (connection != null) {
            String sql = "select * from smbms_user where userCode=? and userPassword=?;";
            Object[] params = {userCode, userPassword};
            try {
                ResultSet rs = BaseDao.execute(connection, sql, params);
                while (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getTimestamp("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getTimestamp("modifyDate"));
                }
                BaseDao.close(null, preparedStatement, rs);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return user;
    }

    public int updatePwd(Connection connection, int id, String updatePassword) {
        int execute = 0;
        Object[] params = {updatePassword, id};
        String sql = "update smbms_user set userPassword = ? where id = ?;";

        if (connection != null) {
            try {
                execute = BaseDao.update(connection, sql, params);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                BaseDao.close(connection, null, null);
            }
        }

        return execute;
    }

    public int getUserCount(Connection connection, String userName, int userRole) {
        PreparedStatement preparedStatement = null;
        int count = 0;
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            ArrayList<Object> list = new ArrayList<Object>();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id ");
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            Object[] params = list.toArray();
            try {
                ResultSet resultSet = BaseDao.execute(connection, sql.toString(), params);
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                BaseDao.close(connection, preparedStatement, null);
            }
        }
        return count;
    }

    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) {
        List<User> userList = new ArrayList<User>();

        PreparedStatement preparedStatement = null;

        StringBuffer sql = new StringBuffer();

        List<Object> list = new ArrayList<Object>();

        ResultSet resultSet = null;

        if (connection != null) {
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u , smbms_role r where u.userRole = r.id ");
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);
            Object[] params = list.toArray();
            try {
                resultSet = BaseDao.execute(connection, sql.toString(), params);
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUserCode(resultSet.getString("userCode"));
                    user.setUserName(resultSet.getString("userName"));
                    user.setGender(resultSet.getInt("gender"));
                    user.setBirthday(resultSet.getDate("birthday"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setUserRole(resultSet.getInt("userRole"));
                    user.setUserRoleName(resultSet.getString("userRoleName"));
                    userList.add(user);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                BaseDao.close(connection, preparedStatement, resultSet);
            }
        }
        return userList;
    }

}
