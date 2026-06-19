package com.example.intellipm.controller;

import com.example.intellipm.entity.Project;
import com.example.intellipm.entity.Role;
import com.example.intellipm.entity.Task;
import com.example.intellipm.entity.User;
import com.example.intellipm.repository.ProjectRepository;
import com.example.intellipm.repository.UserRepository;
import com.example.intellipm.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskController(TaskService taskService,
                          UserRepository userRepository,
                          ProjectRepository projectRepository) {
        this.taskService        = taskService;
        this.userRepository     = userRepository;
        this.projectRepository  = projectRepository;
    }

    // ✅ CHEF_PROJET peut créer une tâche uniquement dans ses projets
    @PostMapping("/project/{projectId}")
    public Task ajouterTask(@PathVariable Long projectId,
                            @RequestBody Task task,
                            Authentication auth) {
        User user = getUser(auth);
        if (user.getRole() == Role.CHEF_PROJET) {
            verifierChefDuProjet(user, projectId);
        } else if (user.getRole() == Role.MEMBRE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Les membres ne peuvent pas créer de tâches.");
        }
        return taskService.ajouterTask(projectId, task);
    }

    @GetMapping
    public List<Task> afficherTasks() {
        return taskService.afficherTasks();
    }

    // Chaque rôle voit ses tâches filtrées
    @GetMapping("/project/{projectId}")
    public List<Task> afficherTasksParProjet(@PathVariable Long projectId,
                                             Authentication auth) {
        User user = getUser(auth);

        if (user != null && user.getRole() == Role.MEMBRE) {
            return taskService.afficherTasksParProjetEtUser(projectId, user.getId());
        }
        return taskService.afficherTasksParProjet(projectId);
    }

    @GetMapping("/{id}")
    public Task afficherTaskParId(@PathVariable Long id) {
        return taskService.afficherTaskParId(id);
    }

    // ✅ CHEF_PROJET peut modifier une tâche uniquement dans ses projets
    @PutMapping("/{id}")
    public Task modifierTask(@PathVariable Long id,
                             @RequestBody Task task,
                             Authentication auth) {
        User user = getUser(auth);
        if (user.getRole() == Role.CHEF_PROJET) {
            Task existing = taskService.afficherTaskParId(id);
            verifierChefDuProjet(user, existing.getProject().getId());
        } else if (user.getRole() == Role.MEMBRE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Les membres ne peuvent pas modifier les tâches.");
        }
        return taskService.modifierTask(id, task);
    }

    // ✅ CHEF_PROJET peut supprimer une tâche uniquement dans ses projets
    @DeleteMapping("/{id}")
    public void supprimerTask(@PathVariable Long id, Authentication auth) {
        User user = getUser(auth);
        if (user.getRole() == Role.CHEF_PROJET) {
            Task existing = taskService.afficherTaskParId(id);
            verifierChefDuProjet(user, existing.getProject().getId());
        } else if (user.getRole() == Role.MEMBRE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Les membres ne peuvent pas supprimer les tâches.");
        }
        taskService.supprimerTask(id);
    }

    // ✅ MEMBRE peut changer le statut de ses tâches assignées
    @PatchMapping("/{id}/statut")
    public Task modifierStatut(@PathVariable Long id,
                               @RequestParam String statut,
                               Authentication auth) {
        User user = getUser(auth);
        if (user.getRole() == Role.MEMBRE) {
            Task task = taskService.afficherTaskParId(id);
            boolean assigneALui = task.getAssignedUser() != null
                    && task.getAssignedUser().getId().equals(user.getId());
            if (!assigneALui) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Vous ne pouvez modifier que le statut de vos propres tâches.");
            }
        }
        return taskService.modifierStatut(id, statut);
    }

    // ===== HELPERS =====

    private User getUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    private void verifierChefDuProjet(User chef, Long projectId) {
        Project projet = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Projet introuvable : " + projectId));
        boolean isChef = projet.getChefProjet() != null
                && projet.getChefProjet().getId().equals(chef.getId());
        if (!isChef) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Vous ne pouvez gérer les tâches que de vos propres projets.");
        }
    }
}