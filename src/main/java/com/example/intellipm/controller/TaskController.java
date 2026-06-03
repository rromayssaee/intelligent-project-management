package com.example.intellipm.controller;

import com.example.intellipm.entity.Task;
import com.example.intellipm.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/project/{projectId}")
    public Task ajouterTask(@PathVariable Long projectId,
                            @RequestBody Task task) {

        return taskService.ajouterTask(projectId, task);
    }

    @GetMapping
    public List<Task> afficherTasks() {
        return taskService.afficherTasks();
    }

    @GetMapping("/project/{projectId}")
    public List<Task> afficherTasksParProjet(@PathVariable Long projectId) {
        return taskService.afficherTasksParProjet(projectId);
    }

    @GetMapping("/{id}")
    public Task afficherTaskParId(@PathVariable Long id) {
        return taskService.afficherTaskParId(id);
    }

    @PutMapping("/{id}")
    public Task modifierTask(@PathVariable Long id,
                             @RequestBody Task task) {

        return taskService.modifierTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void supprimerTask(@PathVariable Long id) {
        taskService.supprimerTask(id);
    }
}