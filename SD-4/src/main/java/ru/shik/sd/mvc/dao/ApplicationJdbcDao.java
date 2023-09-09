package ru.shik.sd.mvc.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class ApplicationJdbcDao extends JdbcDaoSupport {

    protected JdbcTemplate getJdbcTemplateSafe() {
        JdbcTemplate template = getJdbcTemplate();
        if (template == null) {
            throw new RuntimeException("Not found JdbcTemplate");
        }
        return template;
    }
}
