package ru.shik.sd.mvc.dao;

import java.util.List;
import java.util.Optional;

import ru.shik.sd.mvc.model.Task;

public interface TaskDao {

    int addTask(Task task);

    Optional<Task> getTask(int id);

    List<Task> getTasks();

    List<Task> getByIds(List<Integer> ids);

    void completeTask(int id);
}
