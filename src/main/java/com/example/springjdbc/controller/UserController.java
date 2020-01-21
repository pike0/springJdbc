package com.example.springjdbc.controller;

import com.example.springjdbc.dto.User;
import com.example.springjdbc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {
    //和 DataSource 一样，Spring 容器中的 JdbcTemplate 也是有两个，
    // 因此不能通过 byType 的方式注入进来，这里给大伙提供了两种注入思路，一种是使用 @Resource 注解，
    // 直接通过 byName 的方式注入进来，另外一种就是 @Autowired 注解加上 @Qualifier 注解，两者联合起来，
    // 实际上也是 byName。将 JdbcTemplate 注入进来之后，jdbcTemplateOne 和 jdbcTemplateTwo 此时就代表操作不同的数据源，使
    // 用不同的 JdbcTemplate 操作不同的数据源，实现了多数据源配置。

    @Autowired
    @Qualifier("jdbcTemplateOne")
    JdbcTemplate jdbcTemplateOne;
    @Resource(name = "jdbcTemplateTwo")
    JdbcTemplate jdbcTemplateTwo;

    UserService UserService;

    @GetMapping("/user")
    public List<User> getAllUser() {
        //List<User> list = jdbcTemplateOne.query("select * from t_user", new BeanPropertyRowMapper<>(User.class));
        List<User> list = UserService.getAllUsers2();
        return list;
    }
    @GetMapping("/user2")
    public List<User> getAllUser2() {
        //List<User> list = jdbcTemplateTwo.query("select * from t_user", new BeanPropertyRowMapper<>(User.class));
        List<User> list = UserService.getAllUsers3();
        return list;
    }
}
