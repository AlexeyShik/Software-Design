package ru.shik.sd.mvc.dao;

import java.util.List;
import java.util.Optional;

import ru.shik.sd.mvc.model.Checklist;

public interface ChecklistDao {

    int addChecklist(Checklist checklist);

    Optional<Checklist> getChecklist(int id);

    List<Checklist> getChecklists();

    void deleteChecklist(int id);

    void assignTask(int taskId, int checklistId);
}
