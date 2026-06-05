package com.example.intellipm.entity;

import com.example.intellipm.security.encryption.AttributeEncryptor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private String statut;
    private String priorite;
    private LocalDate dateEcheance;
    private Integer estimationHeures;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties("tasks")
    private Project project;

    // ===== ASSIGNATION =====
    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    @JsonIgnoreProperties({"teams", "motDePasseHash"})
    private User assignedUser;

    public Task() {}

    public Long getId() { return id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getPriorite() { return priorite; }
    public void setPriorite(String priorite) { this.priorite = priorite; }

    public LocalDate getDateEcheance() { return dateEcheance; }
    public void setDateEcheance(LocalDate dateEcheance) { this.dateEcheance = dateEcheance; }

    public Integer getEstimationHeures() { return estimationHeures; }
    public void setEstimationHeures(Integer estimationHeures) { this.estimationHeures = estimationHeures; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public User getAssignedUser() { return assignedUser; }
    public void setAssignedUser(User assignedUser) { this.assignedUser = assignedUser; }
}