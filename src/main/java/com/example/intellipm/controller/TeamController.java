package com.example.intellipm.controller;

import com.example.intellipm.entity.Team;
import com.example.intellipm.service.TeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public Team ajouterTeam(@RequestBody Team team) {
        return teamService.ajouterTeam(team);
    }

    @GetMapping
    public List<Team> afficherTeams() {
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
    public String supprimerTeam(@PathVariable Long id) {
        teamService.supprimerTeam(id);
        return "Équipe supprimée avec succès";
    }

    @PutMapping("/{teamId}/users/{userId}")
    public Team ajouterUtilisateurEquipe(@PathVariable Long teamId,
                                         @PathVariable Long userId) {

        return teamService.ajouterUtilisateurEquipe(teamId, userId);
    }
}