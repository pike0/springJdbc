package com.example.springjdbc.service;

import com.example.springjdbc.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.util.List;

@Service
public class UserService {
    //和 DataSource 一样，Spring 容器中的 JdbcTemplate 也是有两个，
    // 因此不能通过 byType 的方式注入进来，这里给大伙提供了两种注入思路，一种是使用 @Resource 注解，
    // 直接通过 byName 的方式注入进来，另外一种就是 @Autowired 注解加上 @Qualifier 注解，两者联合起来，
    // 实际上也是 byName。将 JdbcTemplate 注入进来之后，jdbcTemplateOne 和 jdbcTemplateTwo 此时就代表操作不同的数据源，使
    // 用不同的 JdbcTemplate 操作不同的数据源，实现了多数据源配置。
    @Qualifier("jdbcTemplateOne")
    JdbcTemplate jdbcTemplateOne;
    @Resource(name = "jdbcTemplateTwo")
    JdbcTemplate jdbcTemplateTwo;


    public int addUser(User user) {
        return jdbcTemplateOne.update("insert into user (username,address) values (?,?);", user.getUsername(), user.getAddress());
    }
    //返回的参数是影响的行数

    public int addUser2(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcTemplateOne.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("insert into user (username,address) values (?,?);", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getAddress());
                return ps;
            }
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        System.out.println(user);
        return update;
    }

    public int deleteUserById(Long id) {
        return jdbcTemplateOne.update("delete from user where id=?", id);
    }

    public int updateUserById(User user) {
        return jdbcTemplateOne.update("update user set username=?,address=? where id=?", user.getUsername(), user.getAddress(),user.getId());
    }



    //将数据库中的字段和对象的属性一一对应起来
    public List<User> getAllUsers() {
        return jdbcTemplateOne.query("select * from user", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                String username = resultSet.getString("username");
                String address = resultSet.getString("address");
                long id = resultSet.getLong("id");
                User user = new User();
                user.setAddress(address);
                user.setUsername(username);
                user.setId(id);
                return user;
            }
        });
    }

    public List<User> getAllUsers2() {
        return jdbcTemplateOne.query("select * from user", new BeanPropertyRowMapper<>(User.class));
    }
    public List<User> getAllUsers3() {
        return jdbcTemplateTwo.query("select * from user", new BeanPropertyRowMapper<>(User.class));
    }
}