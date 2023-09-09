package ru.shik.sd.mvc.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import ru.shik.sd.mvc.model.Checklist;

public class ChecklistJdbcDao extends ApplicationJdbcDao implements ChecklistDao {

    private static final String CREATE_CHECKLISTS_SQL = "CREATE TABLE IF NOT EXISTS CHECKLISTS ("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        + "name VARCHAR(30) NOT NULL, "
        + "description VARCHAR(100) NOT NULL"
        + ");";
    private static final String CREATE_TASK_CHECKLISTS_SQL = "CREATE TABLE IF NOT EXISTS TASKCHECKLISTS ("
        + "task_id INTEGER NOT NULL REFERENCES TASKS(id), "
        + "checklist_id INTEGER NOT NULL REFERENCES CHECKLISTS(id), "
        + "PRIMARY KEY (task_id, checklist_id)"
        + ");";
    private static final String NEW_SQL = "INSERT INTO CHECKLISTS (name, description) "
        + "VALUES (?1, ?2); ";
    private static final String  SELECT_CURRENT_ID = "SELECT id FROM CHECKLISTS "
        + "WHERE name = ?1 AND description = ?2 "
        + "ORDER BY id DESC LIMIT 1;";
    private static final String GET_SQL = "SELECT * FROM CHECKLISTS WHERE id = ?;";
    private static final String GET_ALL_SQL = "SELECT * FROM CHECKLISTS;";
    private static final String GET_TASK_IDS_SQL = "SELECT task_id from TASKCHECKLISTS WHERE checklist_id = ?";
    private static final String DELETE_SQL = "DELETE FROM CHECKLISTS WHERE id = ?";
    private static final String DELETE_TASKS_FROM_CHECKLIST_SQL = "DELETE FROM TASKCHECKLISTS WHERE checklist_id = ?";
    private static final String ASSIGN_SQL = "INSERT INTO TASKCHECKLISTS (task_id, checklist_id) VALUES (?1, ?2);";

    public ChecklistJdbcDao(DataSource dataSource) {
        super();
        setDataSource(dataSource);
        getJdbcTemplateSafe().execute(CREATE_CHECKLISTS_SQL);
        getJdbcTemplateSafe().execute(CREATE_TASK_CHECKLISTS_SQL);
    }

    @Override
    public int addChecklist(Checklist checklist) {
        getJdbcTemplateSafe().update(NEW_SQL, checklist.getName(), checklist.getDescription());
        Integer id = getJdbcTemplateSafe()
            .queryForObject(SELECT_CURRENT_ID, Integer.class, checklist.getName(), checklist.getDescription());
        if (id == null) {
            throw new IllegalStateException("Null returned from database after put item");
        }
        return id;
    }

    @Override
    public Optional<Checklist> getChecklist(int id) {
        List<Checklist> checklists = getJdbcTemplateSafe().query(GET_SQL, new BeanPropertyRowMapper<>(Checklist.class), id);
        if (checklists.isEmpty()) {
            return Optional.empty();
        }
        Checklist checklist = checklists.get(0);
        List<Integer> taskIds = getJdbcTemplateSafe().queryForList(GET_TASK_IDS_SQL, Integer.class, id);
        checklist.setTaskIds(taskIds);
        return Optional.of(checklist);
    }

    @Override
    public List<Checklist> getChecklists() {
        return getJdbcTemplateSafe().query(GET_ALL_SQL, new BeanPropertyRowMapper<>(Checklist.class));
    }

    @Override
    public void deleteChecklist(int id) {
        getJdbcTemplateSafe().update(DELETE_SQL, id);
        getJdbcTemplateSafe().update(DELETE_TASKS_FROM_CHECKLIST_SQL, id);
    }

    @Override
    public void assignTask(int taskId, int checklistId) {
        getJdbcTemplateSafe().update(ASSIGN_SQL, taskId, checklistId);
    }
}
