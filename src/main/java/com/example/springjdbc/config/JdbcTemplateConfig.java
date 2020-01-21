package com.example.springjdbc.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
@Configuration
public class JdbcTemplateConfig {
    //每一个 JdbcTemplate 的创建都需要一个 DataSource，
    // 由于 Spring 容器中现在存在两个 DataSource，默认使用类型查找，会报错，
    // 因此加上 @Qualifier 注解，表示按照名称查找。这里创建了两个 JdbcTemplate 实例，分别对应了两个 DataSource。
        @Bean
        JdbcTemplate jdbcTemplateOne(@Qualifier("dsOne") DataSource dsOne) {
            return new JdbcTemplate(dsOne);
        }
        @Bean
        JdbcTemplate jdbcTemplateTwo(@Qualifier("dsTwo") DataSource dsTwo) {
            return new JdbcTemplate(dsTwo);
        }

}
