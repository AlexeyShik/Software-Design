package ru.shik.sd.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ru.shik.sd.dao.ClientDao;
import ru.shik.sd.dao.ClientDaoImpl;

@Configuration
public class JDBCDaoConfiguration {

    @Bean
    public ClientDao turnstileJDBCDao(DataSource dataSource) {
        return new ClientDaoImpl(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:./client");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
