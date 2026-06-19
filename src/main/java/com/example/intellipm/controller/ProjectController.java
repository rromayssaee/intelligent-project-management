package com.example.intellipm.controller;

import com.example.intellipm.entity.Project;
import com.example.intellipm.entity.Role;
import com.example.intellipm.entity.User;
import com.example.intellipm.repository.UserRepository;
import com.example.intellipm.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserRepository userRepository;

    public ProjectController(ProjectService projectService,
                             UserRepository userRepository) {
        this.projectService = projectService;
        this.userRepository = userRepository;
    }

    // ✅ Seul l'ADMIN peut créer un projet
    @PostMapping
    public Project ajouterProject(@Valid @RequestBody Project project,
                                  Authentication auth) {
        User user = getUser(auth);
        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Seul l'administrateur peut créer un projet.");
        }
        return projectService.ajouterProject(project);
    }

    // Chaque rôle voit ses projets filtrés
    @GetMapping
    public List<Project> afficherProjects(Authentication auth) {
        User user = getUser(auth);
        if (user == null) return List.of();

        return switch (user.getRole()) {
            case MEMBRE     -> projectService.afficherProjectsParUser(user.getId());
            case CHEF_PROJET -> projectService.afficherProjectsParChef(user.getId());
            default          -> projectService.afficherProjects(); // ADMIN
        };
    }

    @GetMapping("/{id}")
    public Project afficherProjectParId(@PathVariable Long id) {
        return projectService.afficherProjectParId(id);
    }

    // ✅ CHEF_PROJET peut modifier uniquement ses propres projets
    @PutMapping("/{id}")
    public Project modifierProject(@PathVariable Long id,
                                   @Valid @RequestBody Project project,
                                   Authentication auth) {
        User user = getUser(auth);

        if (user.getRole() == Role.CHEF_PROJET) {
            Project existing = projectService.afficherProjectParId(id);
            boolean isChef = existing.getChefProjet() != null
                    && existing.getChefProjet().getId().equals(user.getId());
            if (!isChef) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Vous ne pouvez modifier que vos propres projets.");
            }
            // Le chef ne peut pas changer le chefProjet ni l'équipe
            project.setChefProjet(existing.getChefProjet());
            project.setTeam(existing.getTeam());
        }

        return projectService.modifierProject(id, project);
    }

    // ✅ Seul l'ADMIN peut supprimer un projet
    @DeleteMapping("/{id}")
    public void supprimerProject(@PathVariable Long id, Authentication auth) {
        User user = getUser(auth);
        if (user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Seul l'administrateur peut supprimer un projet.");
        }
        projectService.supprimerProject(id);
    }

    private User getUser(Authentication auth) {
        return userRepository.findByEmail(auth.getName()).orElse(null);
    }
}