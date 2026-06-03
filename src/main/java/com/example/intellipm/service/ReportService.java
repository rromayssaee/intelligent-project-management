package com.example.intellipm.service;

import com.example.intellipm.dto.ReportDTO;
import com.example.intellipm.entity.Project;
import com.example.intellipm.entity.Task;
import com.example.intellipm.repository.ProjectRepository;
import com.example.intellipm.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import com.example.intellipm.exception.ResourceNotFoundException;

@Service
public class ReportService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ReportService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public ReportDTO genererRapportProjet(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec id : " + projectId));
        List<Task> tasks = taskRepository.findByProjectId(projectId);

        int totalTaches = tasks.size();

        int tachesTerminees = 0;
        int tachesEnCours = 0;
        int tachesEnRetard = 0;

        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            if ("TERMINEE".equalsIgnoreCase(task.getStatut())) {
                tachesTerminees++;
            }

            if ("EN_COURS".equalsIgnoreCase(task.getStatut())) {
                tachesEnCours++;
            }

            if (task.getDateEcheance() != null
                    && task.getDateEcheance().isBefore(today)
                    && !"TERMINEE".equalsIgnoreCase(task.getStatut())) {
                tachesEnRetard++;
            }
        }

        double tauxAvancement = 0;

        if (totalTaches > 0) {
            tauxAvancement = (tachesTerminees * 100.0) / totalTaches;
        }

        return new ReportDTO(
                project.getId(),
                project.getTitre(),
                totalTaches,
                tachesTerminees,
                tachesEnCours,
                tachesEnRetard,
                tauxAvancement
        );
    }
}