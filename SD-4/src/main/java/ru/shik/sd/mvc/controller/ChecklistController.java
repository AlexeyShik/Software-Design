package ru.shik.sd.mvc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.shik.sd.mvc.dao.ChecklistDao;
import ru.shik.sd.mvc.dao.TaskDao;
import ru.shik.sd.mvc.model.Checklist;
import ru.shik.sd.mvc.model.Task;
import ru.shik.sd.mvc.request.AssignRequest;

@Controller
public class ChecklistController {

    private final ChecklistDao checklistDao;
    private final TaskDao taskDao;

    public ChecklistController(ChecklistDao checklistDao, TaskDao taskDao) {
        this.checklistDao = checklistDao;
        this.taskDao = taskDao;
    }

    @RequestMapping(value = "/add-checklist", method = RequestMethod.POST)
    public String addChecklist(@ModelAttribute("checklist") Checklist checklist) {
        int id = checklistDao.addChecklist(checklist);
        return "redirect:/get-checklist?id=" + id;
    }

    @RequestMapping(value = "/assign-task", method = RequestMethod.POST)
    public String assignTask(@ModelAttribute("assignRequest") AssignRequest request) {
        checklistDao.assignTask(request.getTaskId(), request.getChecklistId());
        return "redirect:/get-checklist?id=" + request.getChecklistId();
    }

    @RequestMapping(value = "/get-checklist", method = RequestMethod.GET)
    public String getChecklist(ModelMap map, @RequestParam("id") int id) {
        Optional<Checklist> optionalChecklist = checklistDao.getChecklist(id);
        if (optionalChecklist.isPresent()) {
            Checklist checklist = optionalChecklist.get();

            List<Task> tasks = taskDao.getByIds(checklist.getTaskIds());
            map.addAttribute("checklist", checklist);
            map.addAttribute("tasks", tasks);
            return "checklist";
        }

        return "redirect:/index";
    }

    @RequestMapping(value = "/get-checklists", method = RequestMethod.GET)
    public String getChecklists(ModelMap map) {
        List<Checklist> checklists = checklistDao.getChecklists();
        if (!checklists.isEmpty()) {
            map.addAttribute("checklists", checklists);
            return "checklists";
        }

        return "redirect:/index";
    }

    @RequestMapping(value = "/delete-checklist", method = RequestMethod.POST)
    public String deleteChecklist(@RequestParam("id") int id) {
        checklistDao.deleteChecklist(id);
        return "redirect:/index";
    }

}
