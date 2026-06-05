package com.example.intellipm.service;

import com.example.intellipm.entity.Team;
import com.example.intellipm.entity.User;
import com.example.intellipm.exception.ResourceNotFoundException;
import com.example.intellipm.repository.TeamRepository;
import com.example.intellipm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository,
                       UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team ajouterTeam(Team team) {
        if (team.getDateCreation() == null) {
            team.setDateCreation(LocalDate.now());
        }
        return teamRepository.save(team);
    }

    public List<Team> afficherTeams() {
        return teamRepository.findAll();
    }

    public Team afficherTeamParId(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe introuvable : " + id));
    }

    public Team modifierTeam(Long id, Team nouvelleTeam) {
        Team team = afficherTeamParId(id);
        team.setNom(nouvelleTeam.getNom());
        team.setDescription(nouvelleTeam.getDescription());
        return teamRepository.save(team);
    }

    public void supprimerTeam(Long id) {
        Team team = afficherTeamParId(id);
        teamRepository.delete(team);
    }

    public Team ajouterUtilisateurEquipe(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Equipe introuvable"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!user.getTeams().contains(team)) {
            user.getTeams().add(team);
            userRepository.save(user);
        }

        return teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Equipe introuvable"));
    }

    public Team retirerUtilisateurEquipe(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Equipe introuvable"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.getTeams().remove(team);
        userRepository.save(user);

        return teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Equipe introuvable"));
    }
}