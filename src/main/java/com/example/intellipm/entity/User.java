package com.example.intellipm.entity;

import com.example.intellipm.security.encryption.AttributeEncryptor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate dateInscription;

    // ===== CHAMPS CHIFFRÉS AES-256 =====
    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "telephone")
    private String telephone;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "adresse")
    private String adresse;

    // ===== RELATIONS =====
    @ManyToMany
    @JoinTable(
            name = "user_team",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @JsonIgnoreProperties("users")
    private List<Team> teams = new ArrayList<>();

    public User() {}

    // ===== GETTERS / SETTERS =====
    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasseHash() { return motDePasseHash; }
    public void setMotDePasseHash(String h) { this.motDePasseHash = h; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate d) { this.dateInscription = d; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public List<Team> getTeams() { return teams; }
    public void setTeams(List<Team> teams) { this.teams = teams; }
}