package com.example.intellipm.service;

import com.example.intellipm.entity.Project;
import com.example.intellipm.entity.Team;
import com.example.intellipm.entity.User;
import com.example.intellipm.exception.ResourceNotFoundException;
import com.example.intellipm.repository.ProjectRepository;
import com.example.intellipm.repository.TeamRepository;
import com.example.intellipm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          TeamRepository teamRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository    = teamRepository;
        this.userRepository    = userRepository;
    }

    public Project ajouterProject(Project project) {
        // Associer l'équipe si fournie
        if (project.getTeam() != null && project.getTeam().getId() != null) {
            Team team = teamRepository.findById(project.getTeam().getId()).orElse(null);
            project.setTeam(team);
        }
        // Associer le chef de projet si fourni
        if (project.getChefProjet() != null && project.getChefProjet().getId() != null) {
            User chef = userRepository.findById(project.getChefProjet().getId()).orElse(null);
            project.setChefProjet(chef);
        }
        return projectRepository.save(project);
    }

    public List<Project> afficherProjects() {
        return projectRepository.findAll();
    }

    public List<Project> afficherProjectsParUser(Long userId) {
        return projectRepository.findProjectsByUserId(userId);
    }

    public List<Project> afficherProjectsParChef(Long chefId) {
        return projectRepository.findByChefProjetId(chefId);
    }

    public Project afficherProjectParId(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable : " + id));
    }

    public Project modifierProject(Long id, Project nouveauProject) {
        Project project = afficherProjectParId(id);

        project.setTitre(nouveauProject.getTitre());
        project.setDescription(nouveauProject.getDescription());
        project.setDateDebut(nouveauProject.getDateDebut());
        project.setDateFin(nouveauProject.getDateFin());
        project.setStatut(nouveauProject.getStatut());
        project.setPriorite(nouveauProject.getPriorite());

        // Mettre à jour l'équipe associée
        if (nouveauProject.getTeam() != null && nouveauProject.getTeam().getId() != null) {
            Team team = teamRepository.findById(nouveauProject.getTeam().getId()).orElse(null);
            project.setTeam(team);
        } else {
            project.setTeam(null);
        }

        // Mettre à jour le chef de projet
        if (nouveauProject.getChefProjet() != null && nouveauProject.getChefProjet().getId() != null) {
            User chef = userRepository.findById(nouveauProject.getChefProjet().getId()).orElse(null);
            project.setChefProjet(chef);
        } else {
            project.setChefProjet(null);
        }

        return projectRepository.save(project);
    }

    public void supprimerProject(Long id) {
        Project project = afficherProjectParId(id);
        projectRepository.delete(project);
    }
}