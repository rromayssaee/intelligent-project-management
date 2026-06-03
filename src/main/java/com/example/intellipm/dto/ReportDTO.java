package com.example.intellipm.dto;

public class ReportDTO {

    private Long projectId;
    private String titreProjet;
    private int totalTaches;
    private int tachesTerminees;
    private int tachesEnCours;
    private int tachesEnRetard;
    private double tauxAvancement;

    public ReportDTO() {
    }

    public ReportDTO(Long projectId, String titreProjet, int totalTaches, int tachesTerminees,
                     int tachesEnCours, int tachesEnRetard, double tauxAvancement) {
        this.projectId = projectId;
        this.titreProjet = titreProjet;
        this.totalTaches = totalTaches;
        this.tachesTerminees = tachesTerminees;
        this.tachesEnCours = tachesEnCours;
        this.tachesEnRetard = tachesEnRetard;
        this.tauxAvancement = tauxAvancement;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getTitreProjet() {
        return titreProjet;
    }

    public int getTotalTaches() {
        return totalTaches;
    }

    public int getTachesTerminees() {
        return tachesTerminees;
    }

    public int getTachesEnCours() {
        return tachesEnCours;
    }

    public int getTachesEnRetard() {
        return tachesEnRetard;
    }

    public double getTauxAvancement() {
        return tauxAvancement;
    }
}