package ru.shik.sd.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.shik.sd.mvc.dao.ChecklistDao;
import ru.shik.sd.mvc.dao.ChecklistJdbcDao;
import ru.shik.sd.mvc.dao.TaskJdbcDao;

@Configuration
public class JdbcChecklistDaoContextConfiguration {

    @Bean
    public ChecklistDao checklistJdbcDao(TaskJdbcDao taskJdbcDao) {
        return new ChecklistJdbcDao(taskJdbcDao.getDataSource());
    }
}
