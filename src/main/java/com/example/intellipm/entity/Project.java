package com.example.intellipm.entity;

import com.example.intellipm.security.encryption.AttributeEncryptor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre du projet est obligatoire")
    private String titre;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private String statut;
    private String priorite;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("project")
    private List<Task> tasks = new ArrayList<>();

    // ===== ASSOCIATION PROJET ↔ ÉQUIPE =====
    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnoreProperties("users")
    private Team team;

    public Project() {}

    public Long getId() { return id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }
}