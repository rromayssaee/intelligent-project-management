package com.example.intellipm.controller;

import com.example.intellipm.entity.User;
import com.example.intellipm.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User ajouterUser(@RequestBody User user) {
        return userService.ajouterUser(user);
    }

    @GetMapping
    public List<User> afficherUsers() {
        return userService.afficherUsers();
    }

    @GetMapping("/{id}")
    public User afficherUserParId(@PathVariable Long id) {
        return userService.afficherUserParId(id);
    }

    @PutMapping("/{id}")
    public User modifierUser(@PathVariable Long id, @RequestBody User user) {
        return userService.modifierUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String supprimerUser(@PathVariable Long id) {
        userService.supprimerUser(id);
        return "Utilisateur supprimé avec succès";
    }

    @PostMapping("/{userId}/teams/{teamId}")
    public User ajouterUserAEquipe(@PathVariable Long userId, @PathVariable Long teamId) {
        return userService.ajouterUserAEquipe(userId, teamId);
    }
}