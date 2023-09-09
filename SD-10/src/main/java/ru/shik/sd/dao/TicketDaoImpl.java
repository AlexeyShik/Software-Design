package ru.shik.sd.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ru.shik.sd.model.TicketEvent;

public class TicketDaoImpl extends JdbcDaoSupport implements TicketDao {

    public TicketDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        getJdbcTemplateSafe().execute(
            "CREATE TABLE IF NOT EXISTS TicketEvents ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "ticketId INTEGER NOT NULL,"
                + "time TIMESTAMP NOT NULL,"
                + "expirationTime TIMESTAMP NOT NULL,"
                + "action VARCHAR(6) NOT NULL"
                + ");");
    }

    @Override
    public List<TicketEvent> getEvents(long ticketId) {
        return getJdbcTemplateSafe().query("SELECT * FROM TicketEvents WHERE ticketId = ? ;",
            new BeanPropertyRowMapper<>(TicketEvent.class), ticketId);
    }

    @Override
    public void addEvent(TicketEvent event) {
        getJdbcTemplateSafe().update("INSERT INTO TicketEvents(ticketId, time, expirationTime, action) VALUES (?1, ?2, ?3, ?4)",
            event.getTicketId(), event.getTime().toString(),
            event.getExpirationTime().toString(), event.getAction().name());
    }

    private JdbcTemplate getJdbcTemplateSafe() {
        JdbcTemplate template = getJdbcTemplate();
        if (template == null) {
            throw new RuntimeException("Not found JdbcTemplate");
        }
        return template;
    }
}
