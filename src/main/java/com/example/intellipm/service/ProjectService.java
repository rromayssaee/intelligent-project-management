package com.example.intellipm.service;
import com.example.intellipm.exception.ResourceNotFoundException;

import com.example.intellipm.entity.Project;
import com.example.intellipm.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project ajouterProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> afficherProjects() {
        return projectRepository.findAll();
    }

    public Project afficherProjectParId(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec id : " + id));    }

    public Project modifierProject(Long id, Project nouveauProject) {
        Project project = afficherProjectParId(id);

        project.setTitre(nouveauProject.getTitre());
        project.setDescription(nouveauProject.getDescription());
        project.setDateDebut(nouveauProject.getDateDebut());
        project.setDateFin(nouveauProject.getDateFin());
        project.setStatut(nouveauProject.getStatut());
        project.setPriorite(nouveauProject.getPriorite());

        return projectRepository.save(project);
    }

    public void supprimerProject(Long id) {
        Project project = afficherProjectParId(id);
        projectRepository.delete(project);
    }
}