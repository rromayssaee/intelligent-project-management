package com.example.intellipm.controller;

import com.example.intellipm.entity.Project;
import com.example.intellipm.entity.User;
import com.example.intellipm.repository.UserRepository;
import com.example.intellipm.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Project ajouterProject(@Valid @RequestBody Project project) {
        return projectService.ajouterProject(project);
    }

    @GetMapping
    public List<Project> afficherProjects(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);

        // MEMBRE → uniquement ses projets (équipes dont il fait partie)
        if (user != null && "MEMBRE".equals(user.getRole().name())) {
            return projectService.afficherProjectsParUser(user.getId());
        }

        // ADMIN et CHEF_PROJET → tous les projets
        return projectService.afficherProjects();
    }

    @GetMapping("/{id}")
    public Project afficherProjectParId(@PathVariable Long id) {
        return projectService.afficherProjectParId(id);
    }

    @PutMapping("/{id}")
    public Project modifierProject(@PathVariable Long id,
                                   @Valid @RequestBody Project project) {
        return projectService.modifierProject(id, project);
    }

    @DeleteMapping("/{id}")
    public void supprimerProject(@PathVariable Long id) {
        projectService.supprimerProject(id);
    }
}