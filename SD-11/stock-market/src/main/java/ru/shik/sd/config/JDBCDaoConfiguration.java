package ru.shik.sd.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ru.shik.sd.dao.MarketDao;
import ru.shik.sd.dao.MarketDaoImpl;

@Configuration
public class JDBCDaoConfiguration {

    @Bean
    public MarketDao turnstileJDBCDao(DataSource dataSource) {
        return new MarketDaoImpl(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:./market");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
