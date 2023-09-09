package ru.shik.sd.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ru.shik.sd.model.TurnstileEvent;

public class TurnstileDaoImpl extends JdbcDaoSupport implements TurnstileDao {

    public TurnstileDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        getJdbcTemplateSafe().execute(
            "CREATE TABLE IF NOT EXISTS TurnstileEvents ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + "ticketId INTEGER NOT NULL,"
                + "time TIMESTAMP NOT NULL,"
                + "action VARCHAR(5) NOT NULL"
                + ");");
    }

    @Override
    public List<TurnstileEvent> getEvents() {
        return getJdbcTemplateSafe().query("SELECT * FROM TurnstileEvents ORDER BY time;",
            new BeanPropertyRowMapper<>(TurnstileEvent.class));
    }

    @Override
    public List<TurnstileEvent> getEvents(long ticketId) {
        return getJdbcTemplateSafe().query("SELECT * FROM TurnstileEvents WHERE ticketId = ? ORDER BY time;",
            new BeanPropertyRowMapper<>(TurnstileEvent.class), ticketId);
    }

    @Override
    public List<TurnstileEvent> getEventsAfter(LocalDateTime time) {
        return getJdbcTemplateSafe().query("SELECT * FROM TurnstileEvents WHERE time > ? ORDER BY time;",
            new BeanPropertyRowMapper<>(TurnstileEvent.class), time.toString());
    }

    @Override
    public Optional<TurnstileEvent> getRecentEvent(long ticketId) {
        List<TurnstileEvent> event =  getJdbcTemplateSafe()
            .query("SELECT * FROM TurnstileEvents WHERE ticketId = ? ORDER BY time DESC LIMIT 1;",
            new BeanPropertyRowMapper<>(TurnstileEvent.class), ticketId);
        return event.isEmpty() ? Optional.empty() : Optional.of(event.get(0));
    }

    @Override
    public void addEvent(TurnstileEvent event) {
        getJdbcTemplateSafe().update("INSERT INTO TurnstileEvents(ticketId, time, action) VALUES (?1, ?2, ?3)",
            event.getTicketId(), event.getTime().toString(), event.getAction().name());
    }

    private JdbcTemplate getJdbcTemplateSafe() {
        JdbcTemplate template = getJdbcTemplate();
        if (template == null) {
            throw new RuntimeException("Not found JdbcTemplate");
        }
        return template;
    }
}
