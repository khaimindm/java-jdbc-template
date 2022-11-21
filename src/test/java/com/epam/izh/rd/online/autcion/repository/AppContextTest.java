package com.epam.izh.rd.online.autcion.repository;

import com.epam.izh.rd.online.autcion.configuration.JdbcTemplateConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
//(classes = { JdbcTemplateConfiguration.class })
//@ContextConfiguration(classes = JdbcTemplateConfiguration.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/schema_test.sql", "/data_test.sql"})
@ComponentScan(basePackages = "com.epam.izh.rd.online.autcion")
public class AppContextTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PublicAuction publicAuction;

    @Test
    @DisplayName("Бин DataSource успешно создан")
    public void dataSourceTest() {
        assertDoesNotThrow(() -> dataSource.getConnection().createStatement().execute("SELECT 1 FROM users"), "При доступе к БД произошла ошибка");
    }

}
