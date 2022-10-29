package com.epam.izh.rd.online.autcion.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
@PropertySource("classpath:application.properties")
public class JdbcTemplateConfiguration {

    @Value("${spring.datasource.url}")
    private String url;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        //Connection connection = DriverManager.
        return null;
    }

    @Bean
    public DataSource dataSource() {
        return null;
    }
}
