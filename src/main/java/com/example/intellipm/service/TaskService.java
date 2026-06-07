package com.example.intellipm.service;

import com.example.intellipm.entity.Project;
import com.example.intellipm.entity.Task;
import com.example.intellipm.entity.User;
import com.example.intellipm.exception.ResourceNotFoundException;
import com.example.intellipm.repository.ProjectRepository;
import com.example.intellipm.repository.TaskRepository;
import com.example.intellipm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository  = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository  = userRepository;
    }

    public Task ajouterTask(Long projectId, Task task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable : " + projectId));
        task.setProject(project);

        // Assignation si assignedUserId fourni
        if (task.getAssignedUser() != null && task.getAssignedUser().getId() != null) {
            User user = userRepository.findById(task.getAssignedUser().getId())
                    .orElse(null);
            task.setAssignedUser(user);
            // Mettre le statut à ASSIGNEE automatiquement
            if (user != null && "EN_COURS".equals(task.getStatut())) {
                task.setStatut("ASSIGNEE");
            }
        }

        return taskRepository.save(task);
    }

    public List<Task> afficherTasks() {
        return taskRepository.findAll();
    }

    public List<Task> afficherTasksParProjet(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    // Tâches d'un projet visibles par un membre spécifique
    public List<Task> afficherTasksParProjetEtUser(Long projectId, Long userId) {
        return taskRepository.findByProjectIdAndAssignedUserId(projectId, userId);
    }

    public Task afficherTaskParId(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable : " + id));
    }

    public Task modifierTask(Long id, Task nouvelleTask) {
        Task task = afficherTaskParId(id);

        task.setTitre(nouvelleTask.getTitre());
        task.setDescription(nouvelleTask.getDescription());
        task.setStatut(nouvelleTask.getStatut());
        task.setPriorite(nouvelleTask.getPriorite());
        task.setDateEcheance(nouvelleTask.getDateEcheance());
        task.setEstimationHeures(nouvelleTask.getEstimationHeures());

        // Mettre à jour l'assignation
        if (nouvelleTask.getAssignedUser() != null && nouvelleTask.getAssignedUser().getId() != null) {
            User user = userRepository.findById(nouvelleTask.getAssignedUser().getId()).orElse(null);
            task.setAssignedUser(user);
        } else {
            task.setAssignedUser(null);
        }

        return taskRepository.save(task);
    }

    public void supprimerTask(Long id) {
        Task task = afficherTaskParId(id);
        taskRepository.delete(task);
    }

    public Task modifierStatut(Long id, String statut) {
        Task task = afficherTaskParId(id);
        task.setStatut(statut);
        Task saved = taskRepository.save(task);

        // Vérifier si toutes les tâches du projet sont terminées
        if ("TERMINEE".equals(statut) && saved.getProject() != null) {
            Long projectId = saved.getProject().getId();
            List<Task> allTasks = taskRepository.findByProjectId(projectId);
            boolean allDone = !allTasks.isEmpty() &&
                    allTasks.stream().allMatch(t -> "TERMINEE".equals(t.getStatut()));
            if (allDone) {
                projectRepository.findById(projectId).ifPresent(p -> {
                    p.setStatut("TERMINE");
                    projectRepository.save(p);
                });
            }
        }

        return saved;
    }
}