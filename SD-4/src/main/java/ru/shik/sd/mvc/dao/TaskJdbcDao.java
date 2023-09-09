package ru.shik.sd.mvc.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

import ru.shik.sd.mvc.model.Task;

public class TaskJdbcDao extends ApplicationJdbcDao implements TaskDao {

    private static final String CREATE_TASKS_SQL = "CREATE TABLE IF NOT EXISTS TASKS ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name VARCHAR(30) NOT NULL, "
            + "description VARCHAR(100) NOT NULL, "
            + "complexity INTEGER NOT NULL, "
            + "significance INTEGER NOT NULL, "
            + "completed BOOLEAN NOT NULL DEFAULT FALSE"
            + ");";
    private static final String ADD_SQL = "INSERT INTO TASKS (name, description, complexity, significance) "
        + "VALUES (?1, ?2, ?3, ?4); ";
    private static final String SELECT_CURRENT_ID = "SELECT id FROM TASKS "
        + "WHERE name = ?1 AND description = ?2 AND complexity = ?3 AND significance = ?4 "
        + "ORDER BY id DESC LIMIT 1;";
    private static final String GET_IDS_SQL = "SELECT * FROM TASKS WHERE id = ?;";
    private static final String GET_ALL_SQL = "SELECT * FROM TASKS;";
    private static final String SET_SQL = "UPDATE TASKS SET completed = true WHERE id = ?";

    public TaskJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        getJdbcTemplateSafe().execute(CREATE_TASKS_SQL);
    }

    @Override
    public int addTask(Task task) {
        getJdbcTemplateSafe()
            .update(ADD_SQL, task.getName(), task.getDescription(), task.getComplexity(), task.getSignificance());
        Integer id = getJdbcTemplateSafe()
            .queryForObject(SELECT_CURRENT_ID, Integer.class, task.getName(), task.getDescription(), task.getComplexity(), task.getSignificance());
        if (id == null) {
            throw new IllegalStateException("Null returned from database after put item");
        }
        return id;
    }

    @Override
    public Optional<Task> getTask(int id) {
        List<Task> list = getJdbcTemplateSafe().query(GET_IDS_SQL, new BeanPropertyRowMapper<>(Task.class), id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<Task> getTasks() {
        return getJdbcTemplateSafe().query(GET_ALL_SQL, new BeanPropertyRowMapper<>(Task.class));
    }

    @Override
    public List<Task> getByIds(List<Integer> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        StringBuilder query = new StringBuilder("SELECT * FROM TASKS WHERE id IN (");
        for (int i = 0; i < ids.size(); ++i) {
            query.append(ids.get(i));
            if (i + 1 != ids.size()) {
                query.append(", ");
            }
        }
        query.append(");");
        return getJdbcTemplateSafe().query(query.toString(), new BeanPropertyRowMapper<>(Task.class));
    }

    @Override
    public void completeTask(int id) {
        getJdbcTemplateSafe().update(SET_SQL, id);
    }
}
