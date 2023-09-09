package ru.shik.sd.mvc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shik.sd.mvc.dao.TaskDao;
import ru.shik.sd.mvc.model.Checklist;
import ru.shik.sd.mvc.model.Task;
import ru.shik.sd.mvc.request.AssignRequest;

@Controller
public class TaskController {
    private final TaskDao taskDao;

    public TaskController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @RequestMapping(value = "/add-task", method = RequestMethod.POST)
    public String addTask(@ModelAttribute("task") Task task) {
        int id = taskDao.addTask(task);
        return "redirect:/get-task?id=" + id;
    }

    @RequestMapping(value = "/get-task", method = RequestMethod.GET)
    public String getTask(ModelMap map, @RequestParam("id") int id) {
        Optional<Task> task = taskDao.getTask(id);
        if (task.isPresent()) {
            map.addAttribute("task", task.get());
            return "task";
        }

        map.addAttribute("checklist", new Checklist());
        map.addAttribute("task", new Task());
        return "index";
    }

    @RequestMapping(value = "/get-tasks", method = RequestMethod.GET)
    public String getTasks(ModelMap map) {
        List<Task> tasks = taskDao.getTasks();
        if (!tasks.isEmpty()) {
            map.addAttribute("tasks", tasks);
            return "tasks";
        }

        map.addAttribute("checklist", new Checklist());
        map.addAttribute("task", new Task());
        return "index";
    }

    @RequestMapping(value = "/complete-task", method = RequestMethod.POST)
    public String completeTask(@ModelAttribute("id") int id) {
        taskDao.completeTask(id);
        return "redirect:/get-task?id=" + id;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap map) {
        map.addAttribute("checklist", new Checklist());
        map.addAttribute("task", new Task());
        map.addAttribute("assignRequest", new AssignRequest());
        return "index";
    }

}
