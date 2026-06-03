package com.example.intellipm.service;

import com.example.intellipm.entity.Project;
import com.example.intellipm.entity.Task;
import com.example.intellipm.repository.ProjectRepository;
import com.example.intellipm.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.example.intellipm.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public Task ajouterTask(Long projectId, Task task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec id : " + projectId));
        task.setProject(project);
        return taskRepository.save(task);
    }

    public List<Task> afficherTasks() {
        return taskRepository.findAll();
    }

    public List<Task> afficherTasksParProjet(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public Task afficherTaskParId(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable avec id : " + id));
    }

    public Task modifierTask(Long id, Task nouvelleTask) {
        Task task = afficherTaskParId(id);

        task.setTitre(nouvelleTask.getTitre());
        task.setDescription(nouvelleTask.getDescription());
        task.setStatut(nouvelleTask.getStatut());
        task.setPriorite(nouvelleTask.getPriorite());
        task.setDateEcheance(nouvelleTask.getDateEcheance());
        task.setEstimationHeures(nouvelleTask.getEstimationHeures());

        return taskRepository.save(task);
    }

    public void supprimerTask(Long id) {
        Task task = afficherTaskParId(id);
        taskRepository.delete(task);
    }
}