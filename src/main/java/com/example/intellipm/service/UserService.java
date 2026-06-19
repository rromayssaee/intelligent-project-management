package com.example.intellipm.service;

import com.example.intellipm.entity.Team;
import com.example.intellipm.entity.User;
import com.example.intellipm.repository.TeamRepository;
import com.example.intellipm.repository.UserRepository;
import com.example.intellipm.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       TeamRepository teamRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.teamRepository  = teamRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User ajouterUser(User user) {
        // Hasher le mot de passe avant de sauvegarder
        if (user.getMotDePasseHash() != null && !user.getMotDePasseHash().isBlank()) {
            user.setMotDePasseHash(passwordEncoder.encode(user.getMotDePasseHash()));
        }
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
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec id : " + id));
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