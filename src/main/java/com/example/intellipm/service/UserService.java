package com.example.intellipm.service;

import com.example.intellipm.entity.Team;
import com.example.intellipm.entity.User;
import com.example.intellipm.repository.TeamRepository;
import org.springframework.stereotype.Service;
import com.example.intellipm.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import com.example.intellipm.exception.ResourceNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public UserService(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public User ajouterUser(User user) {
        if (user.getDateInscription() == null) {
            user.setDateInscription(LocalDate.now());
        }
        return userRepository.save(user);
    }

    public List<User> afficherUsers() {
        return userRepository.findAll();
    }

    public User afficherUserParId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec id : " + id));
    }

    public User modifierUser(Long id, User nouveauUser) {

        User user = afficherUserParId(id);

        user.setNom(nouveauUser.getNom());
        user.setEmail(nouveauUser.getEmail());
        user.setRole(nouveauUser.getRole());
        user.setTelephone(nouveauUser.getTelephone());
        user.setAdresse(nouveauUser.getAdresse());

        return userRepository.save(user);
    }

    public void supprimerUser(Long id) {
        User user = afficherUserParId(id);
        userRepository.delete(user);
    }

    public User ajouterUserAEquipe(Long userId, Long teamId) {
        User user = afficherUserParId(userId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Équipe introuvable avec id : " + teamId));

        user.getTeams().add(team);

        return userRepository.save(user);
    }
}