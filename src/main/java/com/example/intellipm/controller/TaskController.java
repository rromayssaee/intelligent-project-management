package com.example.intellipm.controller;

import com.example.intellipm.entity.Task;
import com.example.intellipm.entity.User;
import com.example.intellipm.repository.UserRepository;
import com.example.intellipm.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService    = taskService;
        this.userRepository = userRepository;
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
    public List<Task> afficherTasksParProjet(@PathVariable Long projectId,
                                             Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);

        if (user != null) {
            String role = user.getRole().name();
            // MEMBRE voit uniquement ses tâches assignées
            if ("MEMBRE".equals(role)) {
                List<Task> taches = taskService.afficherTasksParProjetEtUser(projectId, user.getId());
                return taches;
            }
        }
        // ADMIN et CHEF_PROJET voient toutes les tâches
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

    @PatchMapping("/{id}/statut")
    public Task modifierStatut(@PathVariable Long id,
                               @RequestParam String statut) {
        return taskService.modifierStatut(id, statut);
    }
}