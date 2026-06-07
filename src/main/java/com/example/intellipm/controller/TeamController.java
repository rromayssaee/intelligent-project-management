package com.example.intellipm.controller;

import com.example.intellipm.entity.Team;
import com.example.intellipm.entity.User;
import com.example.intellipm.repository.UserRepository;
import com.example.intellipm.service.TeamService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserRepository userRepository;

    public TeamController(TeamService teamService, UserRepository userRepository) {
        this.teamService    = teamService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public Team ajouterTeam(@RequestBody Team team) {
        return teamService.ajouterTeam(team);
    }

    @GetMapping
    public List<Team> afficherTeams(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);

        // MEMBRE → uniquement ses équipes
        if (user != null && "MEMBRE".equals(user.getRole().name())) {
            return teamService.afficherTeams().stream()
                    .filter(t -> t.getUsers().stream()
                            .anyMatch(u -> u.getId().equals(user.getId())))
                    .collect(Collectors.toList());
        }

        return teamService.afficherTeams();
    }

    @GetMapping("/{id}")
    public Team afficherTeamParId(@PathVariable Long id) {
        return teamService.afficherTeamParId(id);
    }

    @PutMapping("/{id}")
    public Team modifierTeam(@PathVariable Long id, @RequestBody Team team) {
        return teamService.modifierTeam(id, team);
    }

    @DeleteMapping("/{id}")
    public void supprimerTeam(@PathVariable Long id) {
        teamService.supprimerTeam(id);
    }

    @PutMapping("/{teamId}/users/{userId}")
    public Team ajouterUtilisateurEquipe(@PathVariable Long teamId,
                                         @PathVariable Long userId) {
        return teamService.ajouterUtilisateurEquipe(teamId, userId);
    }

    @DeleteMapping("/{teamId}/users/{userId}")
    public Team retirerUtilisateurEquipe(@PathVariable Long teamId,
                                         @PathVariable Long userId) {
        return teamService.retirerUtilisateurEquipe(teamId, userId);
    }
}