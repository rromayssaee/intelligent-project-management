package com.example.intellipm.controller;

import com.example.intellipm.repository.ProjectRepository;
import com.example.intellipm.repository.TaskRepository;
import com.example.intellipm.repository.TeamRepository;
import com.example.intellipm.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public DashboardController(ProjectRepository projectRepository,
                               TaskRepository taskRepository,
                               UserRepository userRepository,
                               TeamRepository teamRepository) {

        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public Map<String, Long> statistiques() {

        Map<String, Long> stats = new HashMap<>();

        stats.put("projects", projectRepository.count());
        stats.put("tasks", taskRepository.count());
        stats.put("users", userRepository.count());
        stats.put("teams", teamRepository.count());

        return stats;
    }
}