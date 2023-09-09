package ru.shik.sd.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ru.shik.sd.dao.TicketDao;
import ru.shik.sd.dao.TicketDaoImpl;
import ru.shik.sd.dao.TurnstileDao;
import ru.shik.sd.dao.TurnstileDaoImpl;

@Configuration
public class JDBCDaoConfiguration {

    @Bean
    public TicketDao ticketJDBCDao(DataSource dataSource) {
        return new TicketDaoImpl(dataSource);
    }

    @Bean
    public TurnstileDao turnstileJDBCDao(DataSource dataSource) {
        return new TurnstileDaoImpl(dataSource);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:./gym");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }
}
