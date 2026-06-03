package com.example.intellipm.controller;

import com.example.intellipm.entity.Project;
import com.example.intellipm.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public Project ajouterProject(@Valid @RequestBody Project project) {
        return projectService.ajouterProject(project);
    }

    @GetMapping
    public List<Project> afficherProjects() {
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
    public String supprimerProject(@PathVariable Long id) {
        projectService.supprimerProject(id);
        return "Projet supprimé avec succès";
    }
}